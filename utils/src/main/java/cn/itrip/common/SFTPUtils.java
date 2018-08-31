package cn.itrip.common;

/**
 * 文件上传下载工具类
 * */


import com.jcraft.jsch.*;
import com.jcraft.jsch.ChannelSftp.LsEntry;
//import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

/**
 * Created by chenxl on 2018/3/8.
 */
public class SFTPUtils {

//    private static final Logger LOG = org.apache.logging.log4j.LogManager.getLogger(SFTPUtils.class);

    private static final String host = "192.168.12.105";
    private static final int port = 22;
    private static final String username = "root";
    private static final String password = "123456";
    public static final String directory = "/data/itrip/comment";

    private static ChannelSftp sftp;

    private static SFTPUtils instance = null;

    private SFTPUtils() {
    }

    public static SFTPUtils getInstance() {
        if (instance == null) {
            if (instance == null) {
                instance = new SFTPUtils();
                sftp = instance.connect(host, port, username, password);   //获取连接
            }
        }
        return instance;
    }

    /**
     * 连接sftp服务器
     *
     * @param host     主机
     * @param port     端口
     * @param username 用户名
     * @param password 密码
     * @return
     */
    public ChannelSftp connect(String host, int port, String username, String password) {
        ChannelSftp sftp = null;
        try {
            JSch jsch = new JSch();

            Session sshSession = jsch.getSession(username, host, port);
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            System.out.println("SFTP Session connected.");
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            System.out.println("Connected to " + host);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return sftp;
    }

    /**
     * 上传文件
     *
     * @param directory  上传的目录
     * @param uploadFile 要上传的文件
     */
    public boolean upload(String directory, String uploadFile) {
        try {
            sftp.cd(directory);
            File file = new File(uploadFile);
            FileInputStream fileInputStream = new FileInputStream(file);
            sftp.put(fileInputStream, file.getName());
            fileInputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 重命名文件
     * */
    public boolean rename(String uploadFile,String newPath) {
        try {
            sftp.rename(uploadFile,newPath);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    /**
     * 下载文件
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件
     * @param saveFile     存在本地的路径
     */
    public File download(String directory, String downloadFile, String saveFile) {
        try {
            sftp.cd(directory);
            File file = new File(saveFile);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            sftp.get(downloadFile, fileOutputStream);
            fileOutputStream.close();
            return file;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * 下载文件
     *
     * @param downloadFilePath 下载的文件完整目录
     * @param saveFile     存在本地的路径
     */
    public File download(String downloadFilePath, String saveFile) {
        try {
            int i = downloadFilePath.lastIndexOf('/');
            if (i == -1)
                return null;
            sftp.cd(downloadFilePath.substring(0, i));
            File file = new File(saveFile);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            sftp.get(downloadFilePath.substring(i + 1), fileOutputStream);
            fileOutputStream.close();
            return file;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * 删除文件
     *
     * @param directory  要删除文件所在目录
     * @param deleteFile 要删除的文件
     */
    public void delete(String directory, String deleteFile) {
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }

    public void disconnect() {
        try {
            sftp.getSession().disconnect();
        } catch (JSchException e) {
            System.out.println(e.getMessage());
        }
        sftp.quit();
        sftp.disconnect();
    }

    /**
     * 列出目录下的文件
     *
     * @param directory 要列出的目录
     * @throws SftpException
     */
    public Vector<LsEntry> listFiles(String directory) throws SftpException {
        return sftp.ls(directory);
    }

    public static void main(String[] args) throws IOException {
        SFTPUtils sf = SFTPUtils.getInstance();
        sf.upload(directory, "/Users/mac/Desktop/屏幕快照.png");    //上传文件

//        sf.download(directory, "2.png", "C:\\Users\\hp\\Desktop\\1212.png");
//        File download = sf.download("/home/1.png", "C:\\Users\\hp\\Desktop\\122221.png");

//        sf.delete(directory, deleteFile); //删除文件

//        Vector<LsEntry> files = null;        //查看文件列表
//        try {
//            files = sf.listFiles(directory);
//        } catch (SftpException e) {
//            e.printStackTrace();
//        }
//        for (LsEntry file : files) {
//            System.out.println("###\t" + file.getFilename());
//        }
        sf.disconnect();
    }
}

