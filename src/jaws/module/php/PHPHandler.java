package jaws.module.php;

import java.io.File;

import jaws.module.http.HTTPRequest;
import jaws.module.http.HTTPResponse;
import jaws.module.http.RequestMethod;
import jaws.module.net.ContainsHandler;
import jaws.module.net.Handle;

@ContainsHandler
public class PHPHandler {

	static {

		File phpFolder = new File(".");
		if (!phpFolder.exists()) {

		}
	}

	@Handle(extensions = {"php"}, methods = {RequestMethod.GET, RequestMethod.POST})
	public static HTTPResponse handle(HTTPRequest request, HTTPResponse response, File webroot) {



		return response;
	}
}
