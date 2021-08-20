package com.rubin.rpan.util.file.type.context;

import com.rubin.rpan.util.ReflectUtil;
import com.rubin.rpan.util.file.type.FileTypeDefiner;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 文件类型匹配器
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
public class FileTypeContext {

    private static List<FileTypeDefiner> fileTypeDefinerList = new ArrayList<>();

    static {
        Set<Class> allSubTypeOfFileTypeDefiner = ReflectUtil.getAllSubTypeOf(FileTypeDefiner.class, "com.rubin.rpan.util.file.type");
        if (allSubTypeOfFileTypeDefiner != null && !allSubTypeOfFileTypeDefiner.isEmpty()) {
            allSubTypeOfFileTypeDefiner.stream().forEach(subTypeOfFileTypeDefinerClass -> {
                FileTypeDefiner fileTypeDefiner = (FileTypeDefiner) ReflectUtil.createInstance(subTypeOfFileTypeDefinerClass);
                fileTypeDefinerList.add(fileTypeDefiner);
            });
        }
    }

    /**
     * 通过文件名称获取文件类型标识
     *
     * @param filename
     * @return
     */
    public static Integer getFileTypeCode(String filename) {
        if (fileTypeDefinerList.isEmpty()) {
            throw new RuntimeException("获取文件类型失败");
        }
        fileTypeDefinerList = fileTypeDefinerList.stream()
                .sorted(Comparator.comparingInt(FileTypeDefiner::getOrder).reversed())
                .collect(Collectors.toList());
        for (FileTypeDefiner fileTypeDefiner : fileTypeDefinerList) {
            if (fileTypeDefiner.isMatched(filename)) {
                return fileTypeDefiner.getFileTypeCode();
            }
        }
        throw new RuntimeException("获取文件类型失败");
    }

}
