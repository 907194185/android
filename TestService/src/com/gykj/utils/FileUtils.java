package com.gykj.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.StringUtils;

public class FileUtils {
	// 缓存文件头信息-文件头信息  
    public static final HashMap<String, String> mFileTypes = new HashMap<String, String>();  
    static {  
        // 文件类型,不保证正确,自己想判断什么类型自己修正或增加
        mFileTypes.put("FFD8FFE0", "jpg");  
        mFileTypes.put("89504E47", "png");  
        mFileTypes.put("47494638", "gif");
        
        mFileTypes.put("47697420", "txt");  
        mFileTypes.put("D0CF11E0", "doc");  
        mFileTypes.put("504B0304", "docx");
        
        mFileTypes.put("3026B275", "wmv");
        mFileTypes.put("25504446", "pdf");  
        mFileTypes.put("3C3F786D", "xml");  
        mFileTypes.put("68746D6C", "html");  
        mFileTypes.put("52617221", "rar");  
        
        //以下未验证 
        mFileTypes.put("49492A00", "tif");  
        mFileTypes.put("57415645", "wav");  
        mFileTypes.put("41564920", "avi");  
        mFileTypes.put("2E524D46", "rm");  
        mFileTypes.put("000001BA", "mpg");  
        mFileTypes.put("000001B3", "mpg");  
        mFileTypes.put("6D6F6F76", "mov");  
        mFileTypes.put("4D546864", "mid");  
        mFileTypes.put("4D5A9000", "exe/dll");  
        mFileTypes.put("41433130", "dwg");
        mFileTypes.put("38425053", "psd");  
        mFileTypes.put("7B5C7274", "rtf"); 
        mFileTypes.put("44656C69", "eml");
        mFileTypes.put("5374616E", "mdb");  
        mFileTypes.put("25215053", "ps");
    }  
	
	
	

	/**
	 * 判断文件对象是否存在
	 * @param file
	 * @return
	 */
	public static boolean exists(File file){
		return file != null && file.exists();
	}
	
	/**
	 * 判断文件对象是否文件
	 * @param file
	 * @return
	 */
	public static boolean isFile(File file){
		return exists(file) && file.isFile();
	}

	/**
	 * 判断文件对象是否文件夹
	 * @param file
	 * @return
	 */
	public static boolean isDirectory(File file){
		return exists(file) && file.isDirectory();
	}
	
	/**
	 * 将上传的文件进行重命名
	 * @param targetName 需要指定的文件命名，如果不传入，则使用当前时间戳
	 * @param srcName 在这个文件名获取后缀，如果没有.xx后缀 则没有后缀
	 * @return
	 */
	public static String rename(String targetName, String srcName) {
		//命名文件规则，如果没有存入文件名，使用当前时间戳
		if(StringUtils.isEmpty(targetName)){
			targetName = String.valueOf(System.currentTimeMillis());			
		}
		//加后缀
		if (!StringUtils.isEmpty(srcName) && srcName.indexOf(".") != -1) {
			targetName += srcName.substring(srcName.lastIndexOf("."));
		}
		return targetName;
	}

	/**
	 * 根据文件名的后缀获取文件类型
	 * 
	 * @param name
	 * @return
	 */
	public static String getFileTypeBySuffix(String name) {
		return name.substring(name.lastIndexOf(".") + 1);
	}
	
	/** 
     * 根据文件路径获取文件头信息 
     *  
     * @param src 文件内容
     * @return 文件头信息 
     */  
    public static String getFileType(byte[] src) {
    	String key = "";
    	try {
    		byte[] b = ArrayUtils.subarray(src, 0, 4);
    		key = bytesToHexString(b);
		} catch (Exception e) {
		}
    	System.out.println("--key-"+key);
        return mFileTypes.get(key);  
    }  
    
	/** 
     * 根据文件路径获取文件头信息 
     *  
     * @param filePath 文件路径 
     * @return 文件头信息 
     */  
    public static String getFileType(String filePath) {
        return mFileTypes.get(getFileHeader(filePath));  
    }  
  
    /** 
     * 根据文件路径获取文件头信息 
     *  
     * @param filePath 
     *            文件路径 
     * @return 文件头信息 
     */  
    public static String getFileHeader(String filePath) {  
        FileInputStream is = null;  
        String value = null;  
        try {  
            is = new FileInputStream(filePath);  
            byte[] b = new byte[4];  
            /* 
             * int read() 从此输入流中读取一个数据字节。 int read(byte[] b) 从此输入流中将最多 b.length 
             * 个字节的数据读入一个 byte 数组中。 int read(byte[] b, int off, int len) 
             * 从此输入流中将最多 len 个字节的数据读入一个 byte 数组中。 
             */  
            is.read(b, 0, b.length);  
            value = bytesToHexString(b);  
        } catch (Exception e) {  
        } finally {  
            if (null != is) {  
                try {  
                    is.close();  
                } catch (IOException e) {  
                }  
            }  
        }  
        return value;  
    }  
  
    /** 
     * 将要读取文件头信息的文件的byte数组转换成string类型表示 
     *  
     * @param src 
     *            要读取文件头信息的文件的byte数组 
     * @return 文件头信息 
     */  
    private static String bytesToHexString(byte[] src) {  
        StringBuilder builder = new StringBuilder();  
        if (src == null || src.length <= 0) {  
            return null;  
        }  
        String hv;  
        for (int i = 0; i < src.length; i++) {  
            // 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式，并转换为大写  
            hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();  
            if (hv.length() < 2) {  
                builder.append(0);  
            }  
            builder.append(hv);  
        }
        return builder.toString();  
    }  
  
   
	
	

	/**
	 * 获取存放文件的相对路径
	 * 
	 * @param file  文件对象
	 * @param dir  在哪个目录开始截取
	 * @return
	 */
	public static String getRelativePathFormFile(File file, String dir) {
		String path = file.getAbsolutePath().replaceAll("\\\\", "/");
		int index = path.indexOf(dir);
		if (index < 0) index = 0;
		return path.substring(index);
	}

	/**
	 * 删除指定文件夹下所有文件 文件夹完整绝对路径,不删除目录
	 * 
	 * @param path
	 * @return
	 */
	public static boolean delAllFile(String path) {
		return delFile(new File(path), false);
	}

	/**
	 * 删除指定文件,文件夹也行
	 * 
	 * @param file
	 * @param isRemoveDir 是否删除目录
	 * @return
	 */
	public static boolean delFile(File file, Boolean isRemoveDir) {
		boolean flag = false;
		if (!exists(file)) {
			return false;
		}
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				delFile(f, isRemoveDir);
			}
			if(isRemoveDir) file.delete();
		}else{
			flag = file.delete();
		}
		return flag;
	}
	
	
	
	/**
	 * 压缩文件，使用递归深度压缩文件夹
	 * 
	 * @param out 压缩出来的zip
	 * @param inFile 需要压缩的文件或者文件夹
	 * @param base 压缩到zip里面的基础文件夹
	 */
	public static void zip(ZipOutputStream out, File inFile, String base) {
		//先判定参数
		if(out == null) return;
		if(!exists(inFile)) return;
		if (base == null) base = "/";
		//分别处理文件或者是文件夹
		if (inFile.isDirectory()) {
			File[] fl = inFile.listFiles();
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + "/" + fl[i].getName());
			}
		} else {
			FileInputStream in = null;
			try {
				in = new FileInputStream(inFile);
				out.putNextEntry(new ZipEntry(base));
				int b;
				while ((b = in.read()) != -1) {
					out.write(b);
				}
				in.close();
			} catch (IOException e) {
				if (in != null) {
					try {
						in.close();
					} catch (IOException ex) {
					}
				}
			}
		}
	}

}
