package microYoga.rest;

import microYoga.dao.CourseDao;
import microYoga.dao.MemberDao;
import microYoga.dao.NewsDao;
import microYoga.dao.NotificationDao;
import microYoga.dao.OrderDao;
import microYoga.dao.PhotoDao;
import microYoga.dao.ScheduleDao;
import microYoga.dao.TeacherDao;
import microYoga.dao.VideoDao;
import microYoga.model.Course;
import microYoga.model.FileUploadEntity;
import microYoga.model.Member;
import microYoga.model.News;
import microYoga.model.Notification;
import microYoga.model.Order;
import microYoga.model.Photo;
import microYoga.model.PhotoWall;
import microYoga.model.ResponseObject;
import microYoga.model.Schedule;
import microYoga.model.ScheduleWeek;
import microYoga.model.Teacher;
import microYoga.model.Video;
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
import org.springframework.web.bind.annotation.RequestParam;
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
import java.util.ArrayList;
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

    @Autowired
    private CourseDao courseDaoImp;

    @Autowired
    private VideoDao videoDaoImp;

    @Autowired
    private OrderDao orderDaoImp;

    @Autowired
    private ScheduleDao scheduleDaoImp;

    @Autowired
    private MemberDao memberDaoImp;

    @Autowired
    private NotificationDao notificationDaoImp;

    @Autowired
    private NewsDao newsDaoImp;

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
    public ResponseObject getAllPhotoWallWithPhotos(@RequestParam("pageNumber") int pageNumber, @RequestParam("pageSize") int pageSize){
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
    public ResponseObject deletePhotoWall(@RequestParam("id") String id) {

        try {
            photoDaoImp.deletePhotoWall(id);
            return new ResponseObject("ok", "删除成功", id);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/deletePhoto", method = RequestMethod.GET)
    public ResponseObject deletePhoto(@RequestParam("id") String id) {

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

    //region course
    @RequestMapping(value = "/getCourses", method = RequestMethod.GET)
    public ResponseObject getCourses(){
        try {
            List<Course> items = courseDaoImp.getCourses();
            return new ResponseObject("ok", "查询成功", items);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }
    }
    //endregion

    //region video
    @RequestMapping(value = "/getVideos", method = RequestMethod.GET)
    public ResponseObject getVideos(){
        try {
            List<Video> items = videoDaoImp.getVideos();
            return new ResponseObject("ok", "查询成功", items);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }
    }
    //endregion

    //region Orders
    @RequestMapping(value = "/getOneWeekScheduledCourses", method = RequestMethod.GET)
    public ResponseObject getOneWeekScheduledCourses(@RequestParam("memberId") String memberId ){
        try {
            List<ScheduleWeek> items = new ArrayList<ScheduleWeek>();
            Date today = new Date();
            for(int day = 0; day < 7; day++){
                ScheduleWeek item = new ScheduleWeek();
                Date current = CommonUtils.dateAddDay(today, day);
                item.setWeekName(CommonUtils.getWeekName(current));
                item.setShortDate(CommonUtils.getMonthDay(current));

                item.setScheduleExts(scheduleDaoImp.getOneDayScheduledCourses(CommonUtils.getDateStr(current), memberId));

                items.add(item);
            }



            return new ResponseObject("ok", "查询成功", items);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/insertOrder", method = RequestMethod.GET)
    public ResponseObject insertOrder(@RequestParam("scheduleId") String scheduleId, @RequestParam("memberId") String memberId){
        try {
            String id = UUID.randomUUID().toString();
            Order item = new Order();
            item.setId(id);
            item.setScheduleId(scheduleId);
            item.setMemberId(memberId);
            item.setDateTime(CommonUtils.getCurrentDateTime());
            orderDaoImp.insertOrder(item);
            return new ResponseObject("ok", "新增成功", id);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/deleteOrder", method = RequestMethod.GET)
    public ResponseObject deleteOrder(@RequestParam("orderId") String orderId){
        try {
            orderDaoImp.deleteOrder(orderId);
            return new ResponseObject("ok", "删除成功", orderId);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }
    }
    //endregion

    //region Member
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ResponseObject login(@RequestParam("tel") String tel, @RequestParam("password") String password ){
        try {
            Member item = memberDaoImp.authenticateUser(tel,password);
            return new ResponseObject("ok", "查询成功", item);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/getMembers", method = RequestMethod.GET)
    public ResponseObject getMembers() {
        try {
            List<Member> members = memberDaoImp.getMembers();
            return new ResponseObject("ok", "查询成功", members);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/addMember", method = RequestMethod.POST)
    public ResponseObject addMember(@FormParam("name") String name, @FormParam("sex") String sex, @FormParam("tel") String tel, @FormParam("weChat") String weChat,
                                       @FormParam("pwd") String pwd, @FormParam("joinDate") String joinDate, @FormParam("expireDate") String expireDate,
                                       @FormParam("fee") int fee, @FormParam("remark") String remark) {

        try {
            if (memberDaoImp.isMobileExisted(tel)) {
                return new ResponseObject("error", "此用户的手机号已经注册过了");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }

        Member item = new Member();
        item.setId(UUID.randomUUID().toString());
        item.setName(name);
        item.setSex(sex);
        item.setTel(tel);
        item.setWeChat(weChat);
        item.setPassword(pwd);
        item.setJoinDate(joinDate);
        item.setExpireDate(expireDate);
        item.setFee(fee);
        item.setRemark(remark);

        try {
            memberDaoImp.addMember(item);
            return new ResponseObject("ok", "插入成功", item);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/deleteMember", method = RequestMethod.GET)
    public ResponseObject deleteMembers(@RequestParam("id") String id) {
        try {
            memberDaoImp.deleteMember(id);
            return new ResponseObject("ok", "删除成功", id);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }
    }
    //endregion

    //region notification
    @RequestMapping(value = "/getNotificationBriefByCount", method = RequestMethod.GET)
    public ResponseObject getNotificationBriefByCount(@RequestParam(value="topCount") int topCount) {

        try {
            List<Notification> items = notificationDaoImp.getTopNotificationBriefs(topCount);
            return new ResponseObject("ok", "查询成功", items);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/getNotificationById", method = RequestMethod.GET)
    public ResponseObject getNotificationById(@RequestParam(value="id") String id) {

        try {
            Notification item = notificationDaoImp.getNotificationById(id);
            return new ResponseObject("ok", "查询成功", item);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/getNotifications", method = RequestMethod.GET)
    public ResponseObject getNotifications() {
        try {
            List<Notification> notifications = notificationDaoImp.getNotifications();
            return new ResponseObject("ok", "查询成功", notifications);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/addNotification", method = RequestMethod.POST)
    public ResponseObject addNotification(@FormParam("title") String title, @FormParam("content") String content) {

        Notification item = new Notification();
        item.setId(UUID.randomUUID().toString());
        item.setTitle(title);
        item.setContent(content);
        item.setDate(CommonUtils.getCurrentDate());

        try {
            notificationDaoImp.insertNotification(item);
            return new ResponseObject("ok", "插入成功", item);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/deleteNotification", method = RequestMethod.GET)
    public ResponseObject deleteNotifications(@RequestParam("id") String id) {
        try {
            notificationDaoImp.deleteNotification(id);
            return new ResponseObject("ok", "删除成功", id);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }
    }
    //endregion

    //region News
    @RequestMapping(value = "/getAllNewsBrief", method = RequestMethod.GET)
    public ResponseObject getAllNewsBrief() {

        try {
            List<News> items = newsDaoImp.getAllNewsBrief();
            return new ResponseObject("ok", "查询成功", items);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/getNewsById", method = RequestMethod.GET)
    public ResponseObject getNewsById(@RequestParam(value="id") String id) {

        try {
            News item = newsDaoImp.getNewsById(id);
            return new ResponseObject("ok", "查询成功", item);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }
    }
    //endregion

    //region Schedule
    @RequestMapping(value = "/getSchedules", method = RequestMethod.GET)
    public ResponseObject getSchedules() {
        try {
            List<Schedule> schedules = scheduleDaoImp.getSchedules();
            return new ResponseObject("ok", "查询成功", schedules);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/getFullScheduleByDate", method = RequestMethod.GET)
    public ResponseObject getFullScheduleByDate(@RequestParam("date") String date) {
        try {
            List<Schedule> schedules = scheduleDaoImp.getFullScheduleByDate(date);
            return new ResponseObject("ok", "查询成功", schedules);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/addSchedule", method = RequestMethod.POST)
    public ResponseObject addSchedule(@FormParam("teacherId") String teacherId, @FormParam("courseId") String courseId,
                                      @FormParam("startTime") String startTime, @FormParam("endTime") String endTime,
                                      @FormParam("capacity") int capacity) {

        Schedule item = new Schedule();
        item.setId(UUID.randomUUID().toString());
        item.setTeacherId(teacherId);
        item.setCourseId(courseId);
        item.setStartDateTime(startTime);
        item.setEndDateTime(endTime);
        item.setCapacity(capacity);

        try {
            scheduleDaoImp.insertSchedule(item);
            return new ResponseObject("ok", "插入成功", item);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/deleteSchedule", method = RequestMethod.GET)
    public ResponseObject deleteSchedules(@RequestParam("id") String id) {
        try {
            scheduleDaoImp.deleteSchedule(id);
            return new ResponseObject("ok", "删除成功", id);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseObject("error", "系统错误，请联系系统管理员");
        }
    }
    //endregion
}
