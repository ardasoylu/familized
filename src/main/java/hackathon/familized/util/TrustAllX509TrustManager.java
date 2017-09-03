package hackathon.familized.util;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class TrustAllX509TrustManager implements X509TrustManager
{

	private static final X509Certificate[] acceptedIssuers = new X509Certificate[]{ };

	@Override
	public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
	}

	@Override
	public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return ( acceptedIssuers );
	}


}
