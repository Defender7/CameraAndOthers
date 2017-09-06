package demo.optimizel.dn.com.myqqc60.AccessCamera.Camera.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import demo.optimizel.dn.com.myqqc60.R;

public class FileOperateUtil {
	public final static String TAG="FileOperateUtil";
	public final static int ROOT=0;
	public final static int TYPE_IMAGE=1;
	public final static int TYPE_THUMBNAIL=2;
	public final static int TYPE_VIDEO=3;
	public static String rootFolderForVideos = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyQQC";

	public static String getFolderPath(Context context, int type, String rootPath) {
		StringBuilder pathBuilder=new StringBuilder();
		String path= rootFolderForVideos.concat("/video");
//		pathBuilder.append(context.getExternalFilesDir(null).getAbsolutePath());
		pathBuilder.append(path);

//		pathBuilder.append(File.separator);
//		pathBuilder.append(context.getString(R.string.Files));
		if(rootPath!=null){
			pathBuilder.append(rootPath);
			pathBuilder.append(File.separator);
		}
		switch (type) {
		case TYPE_IMAGE:
			pathBuilder.append(context.getString(R.string.Image));
			break;
		case TYPE_VIDEO:
//			pathBuilder.append(context.getString(R.string.Video));
			break;
		case TYPE_THUMBNAIL:
			pathBuilder.append(context.getString(R.string.Thumbnail));
			break;
		default:
			break;
		}
		return pathBuilder.toString();
	}

	public static List<File> listFiles(String file, final String format, String content){
		return listFiles(new File(file), format,content);
	}
	public static List<File> listFiles(String file, final String format){
		return listFiles(new File(file), format,null);
	}

	public static List<File> listFiles(File file, final String extension, final String content){
		File[] files=null;
		if(file==null||!file.exists()||!file.isDirectory())
			return null;
		files=file.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File arg0, String arg1) {
				// TODO Auto-generated method stub
				if(content==null||content.equals(""))
					return arg1.endsWith(extension);
				else {
                    return  arg1.contains(content)&&arg1.endsWith(extension);           		
				}
			}
		});
		if(files!=null){
			List<File> list=new ArrayList<File>(Arrays.asList(files));
			sortList(list, false);
			return list;
		}
		return null;
	}

	public static void sortList(List<File> list, final boolean asc){

		Collections.sort(list, new Comparator<File>() {
			public int compare(File file, File newFile) {
				if (file.lastModified() > newFile.lastModified()) {
					if(asc){
						return 1;
					}else {
						return -1;
					}
				} else if (file.lastModified() == newFile.lastModified()) {
					return 0;
				} else {
					if(asc){
						return -1;
					}else {
						return 1;
					}
				}

			}
		});
	}

	public static String createFileNmae(String extension, String deviceId){
		String saveFileName="";
		String suffixType =".mp4";
		if(deviceId!=null){
			saveFileName = "/whatslive" + deviceId + System.currentTimeMillis() + suffixType;
		}else {
			saveFileName = "/whatslive" + System.currentTimeMillis() + suffixType;
		}
//		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss",Locale.getDefault());
//		String formatDate = format.format(new Date());
//		if(!extension.startsWith("."))
//			extension="."+extension;
		return saveFileName;
	}

	public static boolean deleteThumbFile(String thumbPath, Context context) {
		boolean flag = false;

		File file = new File(thumbPath);
		if (!file.exists()) {
			return flag;
		}

		flag = file.delete();
		String sourcePath=thumbPath.replace(context.getString(R.string.Thumbnail),
				context.getString(R.string.Image));
		file = new File(sourcePath);
		if (!file.exists()) {
			return flag;
		}
		flag = file.delete();
		return flag;
	}

	public static boolean deleteSourceFile(String sourcePath, Context context) {
		boolean flag = false;

		File file = new File(sourcePath);
		if (!file.exists()) {
			return flag;
		}

		flag = file.delete();
		String thumbPath=sourcePath.replace(context.getString(R.string.Image),
				context.getString(R.string.Thumbnail));
		file = new File(thumbPath);
		if (!file.exists()) {
			return flag;
		}
		flag = file.delete();
		return flag;
	}
}