package microYoga.rest;

import microYoga.dao.PhotoDao;
import microYoga.dao.TeacherDao;
import microYoga.model.FileUploadEntity;
import microYoga.model.Photo;
import microYoga.model.PhotoWall;
import microYoga.model.ResponseObject;
import microYoga.model.Teacher;
import microYoga.utils.CommonUtils;
import microYoga.utils.PhotoUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/websiteService")
public class webServices {

    //region private
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${localStore.MappingPath}")
    private String storeMappingPath;
    @Value("${localStore.MappingUrl}")
    private String storeMappingUrl;

    @Autowired
    private PhotoDao photoDaoImp;

    @Autowired
    private TeacherDao teacherDaoImp;

    //region file upload
    @PostMapping("/fileUpload/{requestFileName}/{requestFileType}")
    public ResponseObject uploadFile(@PathVariable String requestFileName, @PathVariable String requestFileType,
                                     HttpServletRequest request, HttpServletResponse response) {
        StandardMultipartHttpServletRequest fileRequest = (StandardMultipartHttpServletRequest) request;
        if (fileRequest == null) {
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }

        MultipartFile sourceFile = fileRequest.getFile(requestFileName);
        String savePath = storeMappingPath;
        String saveUrl = request.getContextPath() + storeMappingUrl;

        if (StringUtils.isNotEmpty(requestFileType)) {
            String requestType = (requestFileType + "/");
            savePath += requestType;
            saveUrl += requestType;
        }

        File folder = new File(savePath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String originalFileName = sourceFile.getOriginalFilename();
        String randomString = String.format("%s-%s", sdf.format(new Date()), originalFileName);
        String randomFileName = savePath + randomString;
        String randomFileUrl = saveUrl + randomString;
        File targetFile = new File(randomFileName);
        try (OutputStream f = new FileOutputStream(targetFile)) {
            f.write(sourceFile.getBytes());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }

        FileUploadEntity fue = new FileUploadEntity();
        fue.setFileName(originalFileName);
        fue.setFileUrl(randomFileUrl);
        fue.setFileType(requestFileType);

        return new ResponseObject("ok", "上传成功", fue);
    }

    @PostMapping("/uploadPhotoWall/{requestFileName}/{photoWallId}")
    public ResponseObject uploadPhotoWall(@PathVariable String requestFileName, @PathVariable String photoWallId,
                                          HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        StandardMultipartHttpServletRequest fileRequest = (StandardMultipartHttpServletRequest) request;
        if (fileRequest == null) {
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }

        MultipartFile sourceFile = fileRequest.getFile(requestFileName);
        String savePath = storeMappingPath;
        String saveUrl = request.getContextPath() + storeMappingUrl;

        String path = ("photos/" + photoWallId + "/");
        savePath += path;
        saveUrl += path;

        File folder = new File(savePath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String originalFileName = sourceFile.getOriginalFilename();
        String dateTime = sdf.format(new Date());
        String randomString = String.format("%s-%s", dateTime, originalFileName);
        String randomThumbString = String.format("%s-thumb-%s", dateTime, originalFileName);
        String randomFileName = savePath + randomString;
        String randomThumbFileName = savePath + randomThumbString;
        String randomFileUrl = saveUrl + randomString;
        String randomThumbFileUrl = saveUrl + randomThumbString;
        File targetFile = new File(randomFileName);
        try (OutputStream f = new FileOutputStream(targetFile)) {
            f.write(sourceFile.getBytes());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }

        //generate thumb picture
        PhotoUtils.generateThumbPicture(randomFileName, randomThumbFileName, 90, 90);
        //save into database
        Photo photo = new Photo();
        photo.setId(UUID.randomUUID().toString());
        photo.setName(originalFileName);
        photo.setPhotoWallId(photoWallId);
        photo.setUrl(randomFileUrl);
        photo.setThumbUrl(randomThumbFileUrl);
        photo.setDate(dateTime);
        photoDaoImp.addPhoto(photo);

        FileUploadEntity fue = new FileUploadEntity();
        fue.setFileName(originalFileName);
        fue.setFileUrl(randomFileUrl);
        fue.setFileType("");

        return new ResponseObject("ok", "上传成功", fue);
    }
    //endregion

    //region photo
    @RequestMapping(value = "/getAllPhotoWallEntities", method = RequestMethod.GET)
    public ResponseObject getAllPhotoWallEntities(){
        try {
            List<PhotoWall> items = photoDaoImp.getAllPhotoWallEntities();
            return new ResponseObject("ok", "查询成功", items);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/getPhotoWallTotalCount", method = RequestMethod.GET)
    public ResponseObject getPhotoWallTotalCount(){
        try {
            int totalCount = photoDaoImp.getPhotoWallTotalCount();
            return new ResponseObject("ok", "查询成功", totalCount);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/getAllPhotoWallWithPhotos", method = RequestMethod.GET)
    public ResponseObject getAllPhotoWallWithPhotos(@FormParam("pageNumber") int pageNumber, @FormParam("pageSize") int pageSize){
        try {
            List<PhotoWall> items = photoDaoImp.getAllPhotoWallWithPhotos(pageNumber, pageSize);
            return new ResponseObject("ok", "查询成功", items);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }
    }


    @RequestMapping(value = "/addPhotoWall", method = RequestMethod.POST)
    public ResponseObject addPhotoWall(@FormParam("name") String name) {

        PhotoWall item = new PhotoWall();
        item.setId(UUID.randomUUID().toString());
        item.setName(name);
        item.setDate(CommonUtils.getCurrentDate());

        try {
            photoDaoImp.addPhotoWall(item);
            return new ResponseObject("ok", "新增成功", item);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/addPhoto", method = RequestMethod.POST)
    public ResponseObject addPhoto(@FormParam("photoWallId") String photoWallId, @FormParam("name") String name,
                                   @FormParam("url") String url, @FormParam("thumbUrl") String thumbUrl) {

        Photo item = new Photo();
        item.setId(UUID.randomUUID().toString());
        item.setPhotoWallId(photoWallId);
        item.setName(name);
        item.setUrl(url);
        item.setThumbUrl(thumbUrl);
        item.setDate(CommonUtils.getCurrentDate());

        try {
            photoDaoImp.addPhoto(item);
            return new ResponseObject("ok", "新增成功", item);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/updatePhotoWall", method = RequestMethod.POST)
    public ResponseObject updatePhotoWall(@FormParam("id") String id, @FormParam("name") String name) {

        PhotoWall item = new PhotoWall();
        item.setId(id);
        item.setName(name);

        try {
            photoDaoImp.updatePhotoWall(item);
            return new ResponseObject("ok", "修改成功", item);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/deletePhotoWall", method = RequestMethod.GET)
    public ResponseObject deletePhotoWall(@FormParam("id") String id) {

        try {
            photoDaoImp.deletePhotoWall(id);
            return new ResponseObject("ok", "删除成功", id);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/deletePhoto", method = RequestMethod.GET)
    public ResponseObject deletePhoto(@FormParam("id") String id) {

        try {
            photoDaoImp.deletePhoto(id);
            return new ResponseObject("ok", "删除成功", id);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }
    }
    //endregion

    //region teacher
    @RequestMapping(value = "/getTeachers", method = RequestMethod.GET)
    public ResponseObject getTeachers(){
        try {
            List<Teacher> items = teacherDaoImp.getTeachers();
            return new ResponseObject("ok", "查询成功", items);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }
    }
    //endregion

}
