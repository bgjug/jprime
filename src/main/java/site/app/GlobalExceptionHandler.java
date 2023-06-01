package site.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Rostislav Dimitrov
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MultipartException.class)
    public String fileSizeException(MultipartException ex, HttpServletRequest req,
                                    RedirectAttributes redirectAttributes){
        logger.warn("Upload file size must be less than 1 Mbyte - caused by " + req.getUserPrincipal().getName());
        redirectAttributes.addFlashAttribute("multipartException", "Upload file size must be less than 1 Mbyte.");
        return "redirect:"+req.getRequestURI();
    }
}
