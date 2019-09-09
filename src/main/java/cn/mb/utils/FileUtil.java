package cn.mb.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;

/**
 * <p>
 *      文件工具类
 * </p>
 *
 * @author 郭海斌
 * @since 2019/7/12
 */
public class FileUtil {

    /**
     * 根据相对路径返回绝对路径
     * @param relPath
     * @return
     * @throws Exception
     */
    public static String getAbsPath(String relPath) throws Exception {
        //获取根目录绝对路径
        Resource resource = new ClassPathResource("/");
        File file = resource.getFile();
        String path = file.getCanonicalPath();
        //替换目录中的\
        String absPath = path.replaceAll("\\\\", "/") + relPath;
        return absPath;
    }

    /**
     * 删除静态资源中的某文件
     * @param filePath
     */
    public static void deleteFile(String filePath) throws Exception {
        //获取根目录绝对路径
        Resource resource = new ClassPathResource("/");
        File file = resource.getFile();
        String path = file.getCanonicalPath();
        //替换目录中的\
        String absPath = path.replaceAll("\\\\", "/") + filePath;
        file = new File(absPath);
        if (file.exists()) {
            boolean delete = file.delete();
            if (!delete) {
                throw new Exception("删除文件失败");
            }
        } else {
            System.out.println("文件不存在...");
        }
    }

    /**
     * 判断文件格式是否正确
     * @param fileName  文件名
     * @param format    格式，例：jpg,png...
     * @return
     */
    public static boolean isValidFormat(String fileName, String format) {
        int index = fileName.lastIndexOf(".");
        if (index == -1) return false;
        String suffix = fileName.substring(index + 1);
        if (format.contains(suffix)) return true;
        return false;
    }

    /**
     * 截图用于主体识别
     * @param inputStream
     * @param left
     * @param top
     * @param width
     * @param height
     * @return
     * @throws Exception
     */
    public static byte[] screenshot(InputStream inputStream, int left, int top, int width, int height) throws Exception {
        // 2.用工具类ImageIO得到BufferedImage对象,将图片信息放入缓存区
        BufferedImage image = ImageIO.read(inputStream);
        // 3.设置截图图片的(x坐标,y坐标,width宽,height高)信息,并返回截切的新图片,存入缓存区
        BufferedImage newImg = image.getSubimage(left, top, width, height);
        // 4.得到图片的输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 5. 将缓存区的图片,利用ImageIO工具类输出到指定位置.
        ImageIO.write(newImg, "jpg", out);
        // 测试输出图
//        OutputStream outputStream = new FileOutputStream("C:\\Users\\ThinkPad\\Desktop\\time-mirror\\a.jpg");
//        OutputStream outputStream = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\新建文件夹\\a.jpg");
//        ImageIO.write(newImg, "jpg", outputStream);
        System.out.println("截图成功...");
        return out.toByteArray();
    }

    /**
     * 将文件存储在dir目录下
     * @param dir           目录
     * @param inputStream   文件流
     * @param fileName      文件名
     * @throws Exception
     */
    public static void uploadFile(String dir, InputStream inputStream, String fileName) throws Exception {
        System.out.println("上传文件中...");
        System.out.println("当前文件为：" + fileName);
        //获取根目录绝对路径
        Resource resource = new ClassPathResource("/");
        File file = resource.getFile();
        String path = file.getCanonicalPath();
        //替换目录中的\
        String absPath = path.replaceAll("\\\\", "/") + dir;
        //获取并创建目录
        createDir(absPath);
        file = new File(absPath);
        if (!file.exists()) {
            file.mkdir();
        }
        //存储文件
        resource = new ClassPathResource(dir);
        String canonicalPath = resource.getFile().getCanonicalPath();
        Files.copy(inputStream, new File(canonicalPath + "\\" + fileName).toPath());
        System.out.println("上传文件成功...");
    }

    /**
     * 递归创建目录
     * @param dir   目录
     */
    public static void createDir(String dir) {
        File file = new File(dir);
        //递归创建目录
        if (!file.exists()) {
            boolean mkdir = file.mkdir();
            if (!mkdir) {
                createDir(file.getParent());
                file.mkdir();
            }
        }
    }

    /**
     * 获取某url上的文件流
     * @param urlAddr   文件地址
     * @return          文件流
     * @throws Exception
     */
    public static InputStream getURLInputStream(String urlAddr) throws Exception {
        URL url = new URL(urlAddr);
        // 打开连接
        URLConnection con = url.openConnection();
        // 输入流
        return con.getInputStream();
    }

}
