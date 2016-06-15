package jaws.module.php;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import jaws.module.http.HTTPRequest;
import jaws.module.http.HTTPResponse;
import jaws.module.http.RequestMethod;
import jaws.module.net.ContainsHandler;
import jaws.module.net.Handle;

@ContainsHandler
public class PHPHandler {

	static {
		
		File phpFolder = new File("php/");
		if (!phpFolder.exists()) {
			File f = new File(PHPHandler.class.getProtectionDomain().getCodeSource().getLocation().getPath());
			JarFile phpJar;
			try {
				phpJar = new JarFile(f.getAbsolutePath().replace("file:", "").replace("!", ""));
				ZipEntry phpZipEntry = phpJar.getEntry("php.zip");
				ZipEntry entry;
				try (InputStream is = phpJar.getInputStream(phpZipEntry);
				     ZipInputStream zis = new ZipInputStream(is)) {
					while ((entry = zis.getNextEntry()) != null) {
						
						String name = entry.getName();
						File file = new File(name);
						if (entry.isDirectory()) {
							file.mkdirs();
							continue;
						}
	
						File parent = file.getParentFile();
						if (parent != null) {
							parent.mkdirs();
						}
	
						FileOutputStream fos = new FileOutputStream(file);
						byte[] bytes = new byte[1024];
						int length;
						while ((length = zis.read(bytes)) >= 0) {
							fos.write(bytes, 0, length);
						}
						fos.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Handle(extensions = {"php"}, methods = {RequestMethod.GET, RequestMethod.POST})
	public static HTTPResponse handle(HTTPRequest request, HTTPResponse response, File webroot) {
		
		try {
			InputStream in = Runtime.getRuntime().exec("php/php.exe " + webroot + request.url()).getInputStream();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len;
			while ((len = in.read(buffer)) != -1) {
			    out.write(buffer, 0, len);
			}
			response.body(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}
}
