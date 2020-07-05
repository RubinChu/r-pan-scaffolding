package com.rubin.rpan.file;

import com.rubin.rpan.RPanApplication;
import com.rubin.rpan.common.constants.Constants;
import com.rubin.rpan.common.response.ServerResponse;
import com.rubin.rpan.service.IFileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;

/**
 * Created by rubin on 2020/5/30.
 */

@SpringBootTest(classes = RPanApplication.class)
@RunWith(SpringRunner.class)
public class FileServiceTest {

    @Autowired
    private IFileService iFileService;

    @Test
    public void listTest() {
        assertServerResponse(iFileService.list("F433A57CA0C84252A093EF7B4CE3953E", Constants.MINUS_ONE_STR, 1));
    }

    @Test
    public void createDirectoryTest() {
        assertServerResponse(iFileService.createDirectory("3BEE89B248FD42B9A27FA76EE2EE8769", "444", 1));
    }

    @Test
    public void updateFileNameTest() {
        assertServerResponse(iFileService.updateFileName("3BEE89B248FD42B9A27FA76EE2EE8769", "111", 1));
    }

    @Test
    public void deleteTest() {
        assertServerResponse(iFileService.delete("588264AE21094B69A10766FB7C421A14", "798B589511074225BC661E9CE117DE5D", 1));
    }

    @Test
    public void uploadTest() {
        assertServerResponse(iFileService.upload(getMultipartFile(), "CF3B59C8DEC24973A9A1B721452287DB", 7));
    }

    @Test
    public void getDirectoryTreeTest() {
        assertServerResponse(iFileService.getDirectoryTree(1));
    }

    @Test
    public void transferTest() {
        assertServerResponse(iFileService.transfer("AB3026A4E8C34F4DB718D2E3F28AA0EA__,__0D3E6958D0DB4959B37F16CA474DA023__,__9EDE30E333854072852F5EFF6CBF52C8", "3BEE89B248FD42B9A27FA76EE2EE8769", "F433A57CA0C84252A093EF7B4CE3953E", 1));
    }

    @Test
    public void copyTest() {
        assertServerResponse(iFileService.copy("3BEE89B248FD42B9A27FA76EE2EE8769__,__B9E961FC1E304C2B9715191E17A06AB1__,__0D3E6958D0DB4959B37F16CA474DA023", "F433A57CA0C84252A093EF7B4CE3953E", "F433A57CA0C84252A093EF7B4CE3953E", 1));
    }

    @Test
    public void searchTest() {
        assertServerResponse(iFileService.search("t", 1));
    }

    @Test
    public void detailTest() {
        assertServerResponse(iFileService.detail("3BEE89B248FD42B9A27FA76EE2EE8769", 1));
    }

    @Test
    public void breadcrumbsTest() {
        assertServerResponse(iFileService.getBreadcrumbs("3BEE89B248FD42B9A27FA76EE2EE8769", 7));
    }

    private MultipartFile getMultipartFile() {
        MultipartFile multipartFile = null;
        try {
            multipartFile = new MockMultipartFile("file", "test.txt", ",multipart/form-data", "test upload content".getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return multipartFile;
    }

    private void assertServerResponse(ServerResponse serverResponse) {
        System.out.println(serverResponse.getData());
        Assert.isTrue(serverResponse.isSuccess(), serverResponse.getMessage());
    }

}
