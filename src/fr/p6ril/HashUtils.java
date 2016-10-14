package fr.p6ril;

import java.security.MessageDigest;
import java.io.FileInputStream;

import java.security.NoSuchAlgorithmException;
import java.io.FileNotFoundException;
import java.io.IOException;

public class HashUtils {
	private String algorithm = null;
	private String fileName = null;
	private String checksum = null;
	private String message = null;

	HashUtils (String algorithm) {
		this.algorithm = algorithm;
	}

	public String getAlgorithm () {
		return (this.algorithm);
	}

	public String getFileName () {
		return (this.fileName);
	}

	public String getChecksum () {
		return (this.checksum);
	}

	public String getMessage () {
		return (this.message);
	}

	public void setAlgorithm (String algorithm) {
		this.algorithm = algorithm;
	}

	public void setFileName (String fileName) {
		this.fileName = fileName;
	}

	public void setChecksum (String checksum) {
		this.checksum = checksum;
	}

	public String compute () {
		byte[] b = new byte[4096];
		int n = 0;
		StringBuilder sb = null;

		try {
			if ( ( this.algorithm != null ) && ( this.fileName != null ) ) {
				this.message = null; // message is only set in case of an error
				this.checksum = null;
				MessageDigest md = MessageDigest.getInstance(this.algorithm);
				FileInputStream fis = new FileInputStream(this.fileName);
				while ( ( n = fis.read(b) ) > 0 ) {
					md.update(b, 0, n);
				}
				b = md.digest();
				sb = new StringBuilder( b.length * 2 );
				for ( int i = 0 ; i < b.length ; i++ ) {
					sb.append( String.format( "%02x", b[i] & 0xff ) );
				}
				this.checksum = sb.toString();
			}
		// error messages display is delayed and centralized to manage localization
		} catch (NoSuchAlgorithmException e) {
			this.message = "error.noSuchAlgorithm";
		} catch (FileNotFoundException e) {
			this.message = "error.fileNotFound";
		} catch (IOException e) {
			this.message = "error.ioException";
		}
		return ( this.checksum );
	}

	public  boolean validate (String checksum) {
		if ( checksum != null ) {
			this.compute();
			return ( ( this.checksum != null ) ? this.checksum.equalsIgnoreCase(checksum) : false );
		} else {
			return (false);
		}
	}
}