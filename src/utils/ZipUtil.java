package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

class Ziper {
	private ZipOutputStream zipOutputStream;
	private boolean isInitialized = false;
	private FileOutputStream fOutputStream;
	private CheckedOutputStream checkedOutputStream;

	public Ziper() {
	}

	public boolean InitilizeStream(String strFileName) {
		boolean bRetValue = false;
		File file = new File(strFileName);

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			fOutputStream = new FileOutputStream(file);
			// checkedOutputStream=new CheckedOutputStream(fOutputStream,new
			// CRC32());
			zipOutputStream = new ZipOutputStream(fOutputStream);
			isInitialized = bRetValue = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return bRetValue;
	}

	public boolean AddFile(String strFileName) {
		boolean bRetValue = false;
		if (isInitialized) {// 1
			File file = new File(strFileName);
			if (file.exists()) {// 2
				ZipEntry zipEntry = new ZipEntry(file.getName());
				try {// try
					FileInputStream fInputStream = new FileInputStream(
							strFileName);
					zipOutputStream.putNextEntry(zipEntry);
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = fInputStream.read(buffer)) > 0) {
						zipOutputStream.write(buffer, 0, len);
					}
					zipOutputStream.closeEntry();
					fInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}// end try
				bRetValue = true;
			}// 2
		}// 1

		return bRetValue;
	}

	public boolean Close() {
		boolean bRetValue = false;
		if (isInitialized && zipOutputStream != null) {// 1
			try {// try
				zipOutputStream.close();
				checkedOutputStream.close();
				fOutputStream.close();
				isInitialized = false;
				bRetValue = true;
			} catch (IOException e) {
				e.printStackTrace();
			}// end try
		}// 2
		return bRetValue;
	}
}

class UnZiper 
{
	private ZipInputStream zipInputStream;
	private boolean isInitialized = false;
	private boolean running = false;
	private FileInputStream fInputStream;
	private CheckedInputStream checkedInputStream;

	public UnZiper() {
	}

	public boolean InitilizeStream(String strFileName) {
		boolean bRetValue = false;
		File file = new File(strFileName);
		if (file.exists() && file.isFile()) 
		{
			try 
			{
				fInputStream = new FileInputStream(file);
				checkedInputStream = new CheckedInputStream(fInputStream, new CRC32());
				zipInputStream = new ZipInputStream(checkedInputStream);
				isInitialized = bRetValue = true;
			} 
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			}
		}
		return bRetValue;
	}

	public boolean UnZipTo(String strDestDirectory) {
		boolean bRetValue = false;
		running = true;
		if (isInitialized && zipInputStream != null) {
			try {
				File directory = new File(strDestDirectory);

				if (directory.exists()) {
					directory.mkdirs();
				}

				ZipEntry entry = zipInputStream.getNextEntry();

				while (entry != null && running) 
				{
					String fileName = entry.getName();
					File fileToExtra = new File(directory, fileName);

					if (entry.isDirectory()) 
					{
						FileUtil.CreateDirectory(directory.getPath() + "/"
								+ entry.getName());
					}
					else 
					{
						String strParentFile = fileToExtra.getParent();
						File parentFile = new File(strParentFile);

						if (!parentFile.exists()) {
							parentFile.mkdirs();
						}

						if (!fileToExtra.exists()) {
							fileToExtra.createNewFile();
						}

						FileOutputStream extraStream = new FileOutputStream(fileToExtra);
						if (extraStream != null && fileToExtra.canWrite()) 
						{
							final int bufferSize = 2048;
							byte[] buffer = new byte[bufferSize];
							int len = zipInputStream.read(buffer, 0, bufferSize);
							while (len > 0) {
								extraStream.write(buffer, 0, len);
								len = zipInputStream.read(buffer, 0, bufferSize);
							}
						}

						extraStream.close();
					}

					zipInputStream.closeEntry();
					entry = zipInputStream.getNextEntry();
				}
				bRetValue = running;
			} catch (Exception e)// IOException e)
			{
				e.printStackTrace();
			}
		}
		return bRetValue;
	}

	public void CancelUnzipTask() {
		running = false;
	}

	public boolean Close() {
		boolean bRetValue = false;
		if (isInitialized && zipInputStream != null) {
			try {
				zipInputStream.close();
				checkedInputStream.close();
				fInputStream.close();
				isInitialized = false;
				bRetValue = true;
			} catch (IOException e) {
			}
		}
		return bRetValue;
	}
}

/**
 * ZIP压缩与解压缩类
 */
public final class ZipUtil {

	private static UnZiper unZiper = new UnZiper();

	/**
	 * 解压
	 * 
	 * @param strZipFileName
	 *            要解压的压缩文件全路径
	 * @param strDestDirectory
	 *            解压后的文件存放目录
	 * @return
	 */
	public static boolean UnZip(String strZipFileName, String strDestDirectory) {
		boolean bRetValue = false;
		if (unZiper.InitilizeStream(strZipFileName)) {
			bRetValue = unZiper.UnZipTo(strDestDirectory);
		}
		unZiper.Close();
		return bRetValue;
	}

	public static void StopUnzip() {
		if (unZiper != null) {
			unZiper.CancelUnzipTask();
		}
	}

	/**
	 * 压缩
	 * 
	 * @param strZipName
	 *            要创建的Zip文件名称
	 * @param strFileNames
	 *            要添加到压缩文件里的文件全路径列表
	 * @return
	 */
	public static boolean Zip(String strZipName, List<String> strFileNames) {
		boolean bRetValue = false;
		Ziper ziper = new Ziper();
		if (ziper.InitilizeStream(strZipName)) {
			for (String strFileName : strFileNames) {
				if (ziper.AddFile(strFileName)) {
					bRetValue = true;
				} else {
					bRetValue = false;
					break;
				}
			}
			ziper.Close();
		}
		return bRetValue;
	}
}
