package com.rubin.rpan.common.util.file.type.context;

import com.rubin.rpan.common.exception.RPanException;
import com.rubin.rpan.common.util.file.type.FileTypeDefiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件类型匹配器
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@Component(value = "fileTypeContext")
public class FileTypeContext {

    @Autowired
    private List<FileTypeDefiner> fileTypeDefinerList;

    /**
     * 通过文件名称获取文件类型标识
     *
     * @param filename
     * @return
     */
    public Integer getFileTypeCode(String filename) {
        fileTypeDefinerList = fileTypeDefinerList.stream()
                .sorted(Comparator.comparingInt(FileTypeDefiner::getOrder).reversed())
                .collect(Collectors.toList());
        for (FileTypeDefiner fileTypeDefiner : fileTypeDefinerList) {
            if (fileTypeDefiner.isMatched(filename)) {
                return fileTypeDefiner.getFileTypeCode();
            }
        }
        throw new RPanException("获取文件类型失败");
    }

}
