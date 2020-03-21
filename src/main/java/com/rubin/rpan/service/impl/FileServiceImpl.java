package com.rubin.rpan.service.impl;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.rubin.rpan.common.base.BaseService;
import com.rubin.rpan.common.constants.Constants;
import com.rubin.rpan.common.response.ServerResponse;
import com.rubin.rpan.entity.FileInfo;
import com.rubin.rpan.entity.FolderTreeNode;
import com.rubin.rpan.service.IFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by rubin on 2019/9/7.
 * 业务处理类
 */

@Service
public class FileServiceImpl extends BaseService implements IFileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    // 压缩文件根目录
    @Value("${file.root.zip-path}")
    private String zipPath;

    // 静态资源转发前缀
    @Value("${file.root.nginx-path}")
    private String baseNginxPath;

    /**
     * 获取文件列表
     *
     * @param filePath
     * @param fileType
     * @param fileName
     * @param session
     * @return
     */
    @Override
    public ServerResponse<List<FileInfo>> getFiles(String filePath, Integer fileType, String fileName, HttpSession session) {
        filePath = getUserFilePath(session, filePath);
        if (Constants.FILE_TYPE_FILE.equals(fileType)) {
            return ServerResponse.createBySuccess(getFileInfoList(filePath));
        }
        return ServerResponse.createBySuccess(getFileInfoList(filePath, fileType, fileName));
    }

    /**
     * 新建文件夹
     *
     * @param filePath
     * @param folderName
     * @param session
     * @return
     */
    @Override
    public ServerResponse<List<FileInfo>> createDirectory(String filePath, String folderName, HttpSession session) {

        filePath = getUserFilePath(session, filePath);

        File rootFile = new File(getFileRealPathStr(filePath, folderName));

        if (rootFile.exists()) {
            return ServerResponse.createByErrorMessage("文件夹已存在");
        }

        if (rootFile.mkdir()) {
            return ServerResponse.createBySuccess(getFileInfoList(filePath));
        }

        return ServerResponse.createByErrorMessage("文件夹创建失败");
    }

    /**
     * 文件重命名
     *
     * @param filePath
     * @param oldName
     * @param newName
     * @param session
     * @return
     */
    @Override
    public ServerResponse<List<FileInfo>> updateFileName(String filePath, String oldName, String newName, HttpSession session) {

        filePath = getUserFilePath(session, filePath);

        File oldFile = new File(getFileRealPathStr(filePath, oldName));
        File newFile = new File(getFileRealPathStr(filePath, newName));

        if (oldName.equals(newName)) {
            return ServerResponse.createByErrorMessage("请填写一个新的文件名称");
        }

        if (newFile.exists()) {
            return ServerResponse.createByErrorMessage("名称已被占用");
        }

        if (oldFile.renameTo(newFile)) {
            return ServerResponse.createBySuccess(getFileInfoList(filePath));
        }
        return ServerResponse.createByErrorMessage("文件重命名失败");
    }

    /**
     * 删除文件(批量)
     *
     * @param filePath
     * @param fileNames
     * @param session
     * @return
     */
    @Override
    public ServerResponse<List<FileInfo>> deleteFile(String filePath, String fileNames, HttpSession session) {

        filePath = getUserFilePath(session, filePath);

        List<String> fileNameList = Splitter.on(Constants.COMMON_SEPARATOR).splitToList(fileNames);

        for (String fileName : fileNameList) {
            File file = new File(getFileRealPathStr(filePath, fileName));
            delFile(file);
        }

        return ServerResponse.createBySuccess(getFileInfoList(filePath));
    }

    /**
     * 文件上传
     *
     * @param file
     * @param filePath
     * @param session
     * @return
     */
    @Override
    public ServerResponse<List<FileInfo>> uploadFile(MultipartFile file, String filePath, HttpSession session) {

        filePath = getUserFilePath(session, filePath);

        if (file.isEmpty()) {
            return ServerResponse.createByErrorMessage("上传文件为空");
        }

        String fileName = file.getOriginalFilename();

        File dest = new File(getFileRealPathStr(filePath, fileName));

        if (!dest.getParentFile().exists()) {
            return ServerResponse.createByErrorMessage("文件夹不存在");
        }

        if (dest.exists()) {
            fileName = getNewFileName(filePath, fileName);
            dest = new File(getFileRealPathStr(filePath, fileName));
        }

        try {
            dest.createNewFile();
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("上传失败");
        }

        return ServerResponse.createBySuccess(getFileInfoList(filePath));
    }

    /**
     * 文件下载
     *
     * @param filePath
     * @param fileNames
     * @param response
     * @param session
     */
    @Override
    public void downloadFile(String filePath, String fileNames, HttpServletResponse response, HttpSession session) {

        filePath = getUserFilePath(session, filePath);

        List<String> fileNameArr = Splitter.on(Constants.COMMON_SEPARATOR).splitToList(fileNames);

        File zipFolder = new File(zipPath);
        if (!zipFolder.exists()) {
            zipFolder.mkdirs();
        }

        File zipFile = new File(zipPath + Constants.VIRGULE_STR + session.getAttribute(Constants.LOGIN_SESSION_KEY) + Constants.ZIP_FILE_NAME_SUFFIX_STR);
        try {
            if (zipFile.exists()) {
                zipFile.delete();
            }
            zipFile.createNewFile();
        } catch (IOException e) {
            logger.error("下载文件失败，异常信息为", e);
        }

        response.reset();

        FileOutputStream fous;
        ZipOutputStream zipOut;
        try {
            fous = new FileOutputStream(zipFile);
            zipOut = new ZipOutputStream(fous);
            zipFile(filePath, fileNameArr, zipOut);
            zipOut.close();
            fous.close();
            downloadZip(zipFile, response);
        } catch (FileNotFoundException e) {
            logger.error("下载文件失败，异常信息为", e);
        } catch (IOException e) {
            logger.error("下载文件失败，异常信息为", e);
        }
    }

    /**
     * 获取文件夹树
     *
     * @param session
     * @return
     */
    @Override
    public ServerResponse<List<FolderTreeNode>> getFolders(HttpSession session) {

        String filePath = Constants.VIRGULE_STR + Constants.EMPTY_STR;

        filePath = getUserFilePath(session, filePath);

        List<FolderTreeNode> result = Lists.newArrayList();
        FolderTreeNode root = getFolders(rootPath + filePath, Constants.EMPTY_STR, session.getAttribute(Constants.LOGIN_SESSION_KEY).toString());
        result.add(root);
        return ServerResponse.createBySuccess(result);
    }

    /**
     * 转移文件(批量)
     *
     * @param filePath
     * @param fileNames
     * @param targetFolder
     * @param session
     * @return
     */
    @Override
    public ServerResponse<List<FileInfo>> transferFile(String filePath, String fileNames, String targetFolder, HttpSession session) {

        filePath = getUserFilePath(session, filePath);
        targetFolder = getUserFilePath(session, targetFolder);

        List<String> fileNameArr = Splitter.on(Constants.COMMON_SEPARATOR).splitToList(fileNames);
        File file, targetFile;
        String fileRealPath;
        if (fileNameArr != null && fileNameArr.size() > 0) {
            for (String fileName : fileNameArr) {
                fileRealPath = getFileRealPathStr(filePath, fileName);
                file = new File(fileRealPath);
                targetFile = new File(getFileRealPathStr(targetFolder, fileName));
                if (!file.exists()) {
                    logger.error("文件{}不存在!", fileRealPath);
                    break;
                }
                file.renameTo(targetFile);
            }

        }
        return ServerResponse.createBySuccess(getFileInfoList(filePath));
    }

    /******************************************************私有****************************************************/

    /**
     * 获取文件夹下面的所有文件列表
     *
     * @param filePath
     * @return
     */
    private List<FileInfo> getFileInfoList(String filePath) {
        return getFileInfoList(filePath, Constants.FILE_TYPE_FILE);
    }

    /**
     * 获取文件夹下面指定类型的文件列表
     *
     * @param filePath
     * @param fileType
     * @return
     */
    private List<FileInfo> getFileInfoList(String filePath, Integer fileType) {
        return getFileInfoList(filePath, fileType, Constants.EMPTY_STR);
    }

    /**
     * 获取文件夹下面指定类型的文件列表 并标记当前文件
     *
     * @param filePath
     * @param fileType
     * @param fileName
     * @return
     */
    private List<FileInfo> getFileInfoList(String filePath, Integer fileType, String fileName) {

        List<FileInfo> result = Lists.newArrayList();

        File rootFile = new File(getFileRealPathStr(filePath, Constants.EMPTY_STR));

        File[] files;

        if (Constants.FILE_TYPE_FILE.equals(fileType)) {
            files = rootFile.listFiles((dir) -> {
                if (dir.isHidden()) {
                    return false;
                }
                return true;
            });
        } else {
            files = rootFile.listFiles((dir) -> {
                if (dir.isDirectory() || dir.isHidden()) {
                    return false;
                }
                if (getFileType(dir).equals(fileType)) {
                    return true;
                }
                return false;
            });
        }

        FileInfo fileInfo;
        String fileItemName;

        for (int i = 0; i < files.length; i++) {
            fileInfo = new FileInfo();
            fileItemName = files[i].getName();
            fileInfo.setFileName(fileItemName);
            fileInfo.setType(getFileType(files[i]));
            fileInfo.setFileShowPath(baseNginxPath + (Constants.VIRGULE_STR.equals(filePath) ? Constants.EMPTY_STR : filePath) + Constants.VIRGULE_STR + fileItemName);
            if (!Strings.isNullOrEmpty(fileName) && fileName.equals(fileItemName)) {
                fileInfo.setIsChecked(Constants.CHECKED);
                result.add(fileInfo);
                continue;
            }
            fileInfo.setIsChecked(Constants.UNCHECKED);
            result.add(fileInfo);
        }

        // 排序
        result.sort(Comparator.comparing(FileInfo::getFileName));

        return result;
    }

    /**
     * 获取文件类型
     *
     * @param file
     * @return
     */
    private Integer getFileType(File file) {
        if (file.isDirectory()) {
            return Constants.FILE_TYPE_DIRECTORY;
        }
        String fileName = file.getName();
        String tag = fileName.substring(fileName.lastIndexOf(Constants.PERIOD_STR) + 1).toUpperCase();
        switch (tag) {
            case "RAR":
            case "ZIP":
            case "CAB":
            case "ISO":
            case "JAR":
            case "ACE":
            case "7Z":
            case "TAR":
            case "GZ":
            case "ARJ":
            case "LZH":
            case "UUE":
            case "BZ2":
            case "Z":
            case "WAR":
                return Constants.FILE_TYPE_ARCHIVE;
            case "XLSX":
            case "XLS":
            case "CSV":
                return Constants.FILE_TYPE_EXCEL;
            case "DOC":
            case "DOCX":
                return Constants.FILE_TYPE_WORD;
            case "PDF":
                return Constants.FILE_TYPE_PDF;
            case "TXT":
                return Constants.FILE_TYPE_TXT;
            case "BMP":
            case "GIF":
            case "PNG":
            case "ICO":
            case "EPS":
            case "PSD":
            case "TGA":
            case "TIFF":
            case "JPG":
            case "JPEG":
                return Constants.FILE_TYPE_IMG;
            case "MP3":
            case "MKV":
            case "MPG":
            case "RM":
            case "WMA":
                return Constants.FILE_TYPE_AUDIO;
            case "AVI":
            case "3GP":
            case "MP4":
            case "FLV":
            case "RMVB":
            case "MOV":
                return Constants.FILE_TYPE_VIDEO;
            case "PPT":
                return Constants.FILE_TYPE_POWERPOINT;
            case "JAVA":
            case "OBJ":
            case "H":
            case "C":
            case "HTML":
            case "NET":
            case "PHP":
            case "CSS":
            case "JS":
            case "FTL":
            case "JSP":
            case "ASP":
                return Constants.FILE_TYPE_CODE;
            default:
                return Constants.FILE_TYPE_FILE;
        }
    }

    /**
     * 删除文件或文件夹
     *
     * @param file
     * @return
     */
    private boolean delFile(File file) {

        if (!file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File childFile : files) {
                delFile(childFile);
            }
        }

        return file.delete();
    }

    /**
     * 获取文件新名称在上传文件时 遇到文件名称重复的情况 处理方案按照操作系统处理 递增在文件名称后面加上括号数字
     *
     * @param filePath
     * @param fileName
     * @return
     */
    private String getNewFileName(String filePath, String fileName) {
        File folder = new File(getFileRealPathStr(filePath, Constants.EMPTY_STR));
        String fileNameWithoutTag = fileName.substring(0, fileName.lastIndexOf(Constants.PERIOD_STR));
        String[] fileNames = folder.list((dir, name) -> name.indexOf(fileNameWithoutTag) != -1);
        int num = 0;
        for (int i = 0, iLength = fileNames.length; i < iLength; ++i) {
            int leftParenthesesIndex = fileNames[i].lastIndexOf(Constants.CN_LEFT_PARENTHESES_STR),
                    rightParenthesesIndex = fileNames[i].lastIndexOf(Constants.CN_RIGHT_PARENTHESES_STR);
            if (leftParenthesesIndex != -1) {
                int currentNum = Integer.valueOf(fileNames[i].substring(leftParenthesesIndex + 1, rightParenthesesIndex));
                if (currentNum > num) {
                    num = currentNum;
                }
            }
        }
        return new StringBuffer()
                .append(fileNameWithoutTag)
                .append(Constants.CN_LEFT_PARENTHESES_STR)
                .append(++num)
                .append(Constants.CN_RIGHT_PARENTHESES_STR)
                .append(Constants.PERIOD_STR)
                .append(fileName.substring(fileName.lastIndexOf(Constants.PERIOD_STR) + 1))
                .toString();
    }

    /**
     * 压缩文件
     *
     * @param filePath
     * @param fileNameArr
     * @param zipOut
     */
    private void zipFile(String filePath, List<String> fileNameArr, ZipOutputStream zipOut) {
        File file;
        for (String fileName : fileNameArr) {
            file = new File(getFileRealPathStr(filePath, fileName));
            zipFile(file, zipOut, fileName);
        }

    }

    /**
     * 压缩文件实现
     *
     * @param file
     * @param zipOut
     */
    private void zipFile(File file, ZipOutputStream zipOut, String base) {
        if (file.exists()) {
            if (file.isFile()) {
                FileInputStream in;
                try {
                    in = new FileInputStream(file);
                    BufferedInputStream bins = new BufferedInputStream(in, 512);
                    zipOut.putNextEntry(new ZipEntry(base));
                    // 向压缩文件中输出数据
                    int nNumber;
                    byte[] buffer = new byte[512];
                    while ((nNumber = bins.read(buffer)) != -1) {
                        zipOut.write(buffer, 0, nNumber);
                    }
                    // 关闭创建的流对象
                    bins.close();
                    in.close();
                } catch (FileNotFoundException e) {
                    logger.error("下载文件失败，异常信息为", e);
                } catch (IOException e) {
                    logger.error("下载文件失败，异常信息为", e);
                }
            } else {
                File[] files = file.listFiles();
                try {
                    if (files.length == 0) {
                        zipOut.putNextEntry(new ZipEntry(base + Constants.VIRGULE_STR));
                    } else {
                        for (int i = 0; i < files.length; i++) {
                            zipFile(files[i], zipOut, base + Constants.VIRGULE_STR + files[i].getName());
                        }
                    }
                } catch (IOException e) {
                    logger.error("下载文件失败，异常信息为", e);
                }


            }

        }
    }

    /**
     * 下载压缩包
     *
     * @param zipFile
     * @param response
     */
    private void downloadZip(File zipFile, HttpServletResponse response) {
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        try {
            response.setHeader("Content-Disposition", "attachment;fileName=" + new String(Constants.ZIP_FILE_NAME_STR.getBytes("GB2312"), "ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            logger.error("下载文件失败，异常信息为", e);
        }
        byte[] buffer = new byte[1024];
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            fis = new FileInputStream(zipFile);
            bis = new BufferedInputStream(fis);
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
        } catch (Exception e) {
            logger.error("下载文件失败，异常信息为", e);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    logger.error("下载文件失败，异常信息为", e);
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    logger.error("下载文件失败，异常信息为", e);
                }
            }
        }
    }

    /**
     * 递归拼装文件夹结构
     *
     * @param filePath
     * @param base
     * @param username
     */
    private FolderTreeNode getFolders(String filePath, String base, String username) {

        File file = new File(filePath);
        File rootFile = new File(rootPath + Constants.VIRGULE_STR + username);
        FolderTreeNode node;
        String fileName;

        if (file.equals(rootFile)) {
            node = assembleNode(Constants.VIRGULE_STR, Constants.VIRGULE_STR);
        } else {
            fileName = file.getName();
            node = assembleNode(fileName, base);
        }
        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            return node;
        } else {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    node.getChildren().add(getFolders(filePath + Constants.VIRGULE_STR + files[i].getName(), base + Constants.VIRGULE_STR + files[i].getName(), username));
                }
            }
            return node;
        }

    }

    /**
     * 拼装树结点实体
     *
     * @param fileName
     * @param filePath
     * @return
     */
    private FolderTreeNode assembleNode(String fileName, String filePath) {
        FolderTreeNode node = new FolderTreeNode();
        node.setTitle(fileName);
        node.setChecked(Boolean.FALSE);
        node.setDisabled(Boolean.FALSE);
        node.setHref(Constants.EMPTY_STR);
        node.setSpread(Boolean.TRUE);
        node.setId(UUID.randomUUID().toString().toUpperCase().replace(Constants.HYPHEN_STR, Constants.EMPTY_STR));
        node.setParentId(filePath);
        node.setChildren(Lists.newArrayList());
        return node;
    }

    /**
     * 获取用户私有文件路径
     *
     * @param session
     * @param filePath
     * @return
     */
    private String getUserFilePath(HttpSession session, String filePath) {
        return Constants.VIRGULE_STR + session.getAttribute(Constants.LOGIN_SESSION_KEY) + filePath;
    }

}

