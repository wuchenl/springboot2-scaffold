package com.letters7.wuchen.springboot2.utils.zip;

import com.letters7.wuchen.springboot2.utils.exception.UtilException;
import com.letters7.wuchen.springboot2.utils.string.UtilString;
import com.letters7.wuchen.springboot2.utils.web.UtilWeb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.zip.NameMapper;
import org.zeroturnaround.zip.ZipUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author wuchenl
 * @date 2018/12/31.
 * @desc 压缩文件工具类
 */
public class UtilZip {
    /**
     * 日志记录器
     */
    private static Logger logger = LoggerFactory.getLogger(UtilZip.class);

    /**
     * 块大小
     */
    private static final int BUFFER_SIZE = 10 * 1024;

    /**
     * 传统的压缩文件或文件夹到指定目录
     *
     * @param sourceFilePath 源文件或文件夹路径
     * @param zipFilePath    压缩后的路径
     * @param zipFileName    压缩后的文件名
     * @return 压缩后的文件名
     */
    public static String fileToZip(String sourceFilePath, String zipFilePath, String zipFileName) {
        File sourceFile = new File(sourceFilePath);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;

        if (!sourceFile.exists()) {
            logger.warn("压缩文件所在路径不存在:{}", sourceFilePath);
            return null;
        }
        File[] sourceFiles = sourceFile.listFiles();
        if (Objects.isNull(sourceFiles) || sourceFiles.length == 0) {
            logger.warn("待压缩文件夹为空:{}", sourceFile.getAbsolutePath());
            return null;
        }
        try {
            File zipFile = new File(UtilString.join(zipFilePath, File.pathSeparator, zipFileName));
            zipFile.deleteOnExit();
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(new BufferedOutputStream(fos));
            byte[] buffs = new byte[BUFFER_SIZE];
            // 遍历。将所有文件加入到压缩包中
            for (File file : sourceFiles) {
                ZipEntry zipEntry = new ZipEntry(file.getName());
                zos.putNextEntry(zipEntry);
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis, BUFFER_SIZE);
                int read = 0;
                while ((read = bis.read(buffs, 0, BUFFER_SIZE)) != -1) {
                    zos.write(buffs, 0, read);
                }
            }
            return zipFile.getName();
        } catch (FileNotFoundException e) {
            logger.error("创建输出流异常:{}", UtilException.getBootMessage(e));
            return null;
        } catch (IOException e) {
            logger.error("压缩文件进压缩包出现异常:{}", UtilException.getBootMessage(e));
            return null;
        } finally {
            try {
                if (Objects.nonNull(fos)) {
                    fos.close();
                }
                if (Objects.nonNull(bis)) {
                    bis.close();
                }
                if (Objects.nonNull(zos)) {
                    zos.close();
                }
                if (Objects.nonNull(fis)) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断对应ZIP文件中是否包含某个文件
     *
     * @param zipPath  zip5文件路径
     * @param fileName 对应文件
     * @return 是否包含
     */
    public static boolean containsFile(String zipPath, String fileName) {
        if (UtilString.isAnyEmpty(zipPath, fileName)) {
            logger.warn("判断压缩包中是否存在对应文件失败，传入参数为空:{},{}", zipPath, fileName);
            return false;
        }
        File zipFile = new File(zipPath);
        if (!zipFile.exists()) {
            logger.warn("加载文件zip包失败:{}", zipPath);
            return false;
        }
        return ZipUtil.containsEntry(zipFile, fileName);
    }

    /**
     * 获取zip包某个文件
     *
     * @param zipPath  zip包路径
     * @param fileName 文件名
     * @return 对应字节信息
     */
    public static byte[] unpackFile(String zipPath, String fileName) {
        return ZipUtil.unpackEntry(new File(zipPath), fileName);
    }

    /**
     * 获取zip包某个文件
     *
     * @param zipPath  zip包路径
     * @param fileName 文件名
     * @param encode   编码
     * @return 对应字节信息
     */
    public static byte[] unpackFile(String zipPath, String fileName, String encode) {
        return ZipUtil.unpackEntry(new File(zipPath), fileName, Charset.forName(encode));
    }



    /**
     * 解压zip包到某个路径
     * @param zipPath zip包原始路径
     * @param filePath 解压后的路径
     */
    public static void unpack(String zipPath, String filePath) {
        ZipUtil.unpack(new File(zipPath), new File(filePath));
    }

    /**
     * 从zip包中解压某个文件为某个指定的文件
     * @param zipPath zip包路径
     * @param fileName zip包的文件名
     * @param filePath 解压后存放的路径以及文件名
     * @return 是否操作成功
     */
    public static boolean unpackFileOut(String zipPath, String fileName, String filePath) {
        return ZipUtil.unpackEntry(new File(zipPath), fileName, new File(filePath));
    }


    /**
     * 压缩指定路径成为一个zip包
     * @param sourcePath 原始文件
     * @param zipPath  压缩包的zip包以及路径
     */
    public static void pack(String sourcePath,String zipPath) {
        ZipUtil.pack(new File(sourcePath), new File(zipPath));
    }

    /**
     * 解压zip在其原始路径处
     * @param zipPath 原始路径
     */
    public static void explode(String zipPath) {
        ZipUtil.explode(new File(zipPath));
    }


//    /**
//     * 压缩zip在其原始路径处
//     * @param zipPath 原始路径
//     */
//    public static void unexplode(String zipPath) {
//        ZipUtil.unexplode(new File(zipPath));
//    }
}
