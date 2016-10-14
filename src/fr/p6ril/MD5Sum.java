package fr.p6ril;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Locale;
import java.io.File;
import java.util.ResourceBundle;
import java.text.MessageFormat;

import java.util.MissingResourceException;

public class MD5Sum {
	private Locale locale = Locale.ENGLISH;
	private String message = null;

	public static void main (String[] args) {
		HashUtils hashUtils = new HashUtils("MD5");
		MD5Sum md5summer = new MD5Sum(args, hashUtils);

		if ( md5summer.message == null ) { // init happened without any error
			String checksum = hashUtils.getChecksum();
			if ( checksum != null ) {
				boolean isvalid = hashUtils.validate(checksum);
				if ( hashUtils.getMessage() == null ) { // validation happened without error
					if ( isvalid ) {
						md5summer.message = "validation.ok";
					} else {
						md5summer.message = "validation.nok";
					}
				}
			} else {
				hashUtils.compute();
				if ( hashUtils.getMessage() == null ) { // hash calculation happened without error
					md5summer.message = "computation.result";
				}
			}
			md5summer.dispatchMessage(hashUtils);
		}
	}

	MD5Sum (String[] args, HashUtils hashUtils) {
		ArrayList<String> options = new ArrayList<String>();
		ArrayList<String> params = new ArrayList<String>();

		// first separate the options (prefixed with a '-') from the parameters
		for ( int i = 0 ; i < args.length ; i++ ) {
			if ( ( args[i].length() > 1 ) && ( args[i].charAt(0) == '-' ) ) {
				options.add(args[i]);
			} else {
				params.add(args[i]);
			}
		}
		// parse the options and set the context accordingly
		if ( options.size() > 0 ) {
			Pattern regex = Pattern.compile("-(SHA-1|SHA-256|FR)", Pattern.CASE_INSENSITIVE); // all java platforms normally support these algorithms
			Matcher matcher = null;
			String group = null;
			for ( String option : options ) {
				if ( matcher == null ) {
					matcher = regex.matcher(option);
				} else {
					matcher.reset(option);
				}
				if ( matcher.matches() ) {
					group = matcher.group(1).toUpperCase(); // group(0) always refers to the entire pattern
					if ( group.equals("FR") ) {
						this.locale = Locale.FRENCH;
					} else {
						hashUtils.setAlgorithm(group);
					}
				}
			}
		}
		// check the parameters and set the object members accordingly
		switch ( params.size() ) {
			case 2:
				String checksum = params.get(1);
				String algorithm = hashUtils.getAlgorithm();
				if ( ( algorithm.equals("MD5") && !checksum.matches("[A-Fa-f\\d]{32}") ) ||
					( algorithm.equals("SHA-1") && !checksum.matches("[A-Fa-f\\d]{40}") ) ||
					( algorithm.equals("SHA-256") && !checksum.matches("[A-Fa-f\\d]{64}") ) ) {
					this.message = "error.invalidChecksum";
				} else {
					hashUtils.setChecksum(checksum);
				}
			case 1:
				if ( this.message == null ) { // no error so far
					String fileName = params.get(0);
					hashUtils.setFileName(fileName);
					File file = new File(fileName);
					if ( !file.exists() ) {
						this.message = "error.fileNotFound";
					}
				}
				break;
			default:
				this.message = "error.illegalArguments";
		}
		this.dispatchMessage(hashUtils);
	}

	private void dispatchMessage (HashUtils hashUtils) {
		try {
			ResourceBundle resource = ResourceBundle.getBundle("MD5Sum", this.locale);
			Object[] messageArguments = {
				hashUtils.getAlgorithm(),
				hashUtils.getFileName(),
				hashUtils.getChecksum()
			};
			MessageFormat formatter = new MessageFormat("", this.locale);
			String message = hashUtils.getMessage(); // first display computation or validation internal error if any
			if ( message != null ) {
				formatter.applyPattern(resource.getString(message));
				System.out.println(formatter.format(messageArguments));
			}
			message = this.message;
			if ( message != null ) {
				formatter.applyPattern(resource.getString(message));
				System.out.println(formatter.format(messageArguments));
				if ( this.message.indexOf("error") >= 0 ) { // init went wrong show help
					System.out.println(resource.getString("help"));
				}
			}
		} catch (MissingResourceException e) {

		}
	}
}