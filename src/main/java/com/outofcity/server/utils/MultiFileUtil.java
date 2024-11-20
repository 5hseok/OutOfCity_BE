package com.outofcity.server.utils;

import com.outofcity.server.global.exception.BusinessException;
import com.outofcity.server.global.exception.message.ErrorMessage;
import org.springframework.web.multipart.MultipartFile;

public class MultiFileUtil {

    public static String determineImageFormat(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType != null) {
            if (contentType.equals("image/jpeg") || contentType.equals("image/jpg"))
                return "jpg";
            else if (contentType.equals("image/png"))
                return "png";
            else
                throw new BusinessException(ErrorMessage.NOT_SUPPORTED_MEDIA_TYPE_ERROR);
        } else
            throw new BusinessException(ErrorMessage.UNDETERMINED_FILE);
    }
}
