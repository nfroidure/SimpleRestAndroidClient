package com.elitwork.simplerestandroidclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

public class RestRequest
	{
	public static final int HTTP_OPTIONS = 1;
	public static final int HTTP_HEAD = 2;
	public static final int HTTP_GET = 4;
	public static final int HTTP_PUT = 8;
	public static final int HTTP_POST = 16;
	public static final int HTTP_DELETE = 32;
	private int method;
	private HttpRequestBase request;
	private HttpResponse response;

	public RestRequest(String uri, int method)
		{
		this.method = method;
		switch (method)
			{
			case HTTP_OPTIONS:
				this.request = new HttpOptions(uri);
			break;
			case HTTP_HEAD:
				this.request = new HttpHead(uri);
			break;
			case HTTP_GET:
				this.request = new HttpGet(uri);
			break;
			case HTTP_PUT:
				this.request = new HttpPut(uri);
			break;
			case HTTP_POST:
				this.request = new HttpPost(uri);
			break;
			case HTTP_DELETE:
				this.request = new HttpDelete(uri);
			}
		}

	public void addHeader(String name, String value)
		{
		this.request.addHeader(name, value);
		}

	public void setRequestContent(String content)
		{
		if (this.method == HTTP_POST ||this.method == HTTP_PUT)
			{
			try
				{
				((HttpEntityEnclosingRequestBase)this.request).setEntity(new StringEntity(content));
				}
			catch (Exception e)
				{
				e.printStackTrace();
				}
			}
		}

	public boolean execute(String username, String password)
		{
		try
			{
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			BasicHttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
			HttpProtocolParams.setUserAgent(params, "SimpleRestAndroidClient");

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			DefaultHttpClient client = new DefaultHttpClient(new ThreadSafeClientConnManager(params, registry), params);
			
			if(username!=""&&password!="")
				{
				CredentialsProvider cp = new BasicCredentialsProvider();
				cp.setCredentials(new AuthScope(AuthScope.ANY_HOST, -1), new UsernamePasswordCredentials(username, password));
				client.setCredentialsProvider(cp);
				}
			try
				{
				this.response = client.execute(this.request);
				}
			catch (IOException e)
				{
				e.printStackTrace();
				return false;
				}
			}
		catch (Exception e)
			{
			e.printStackTrace();
			return false;
			}
		return true;
		}

	public int getResponseCode()
		{
		return this.response
			.getStatusLine()
			.getStatusCode();
		}

	public String getResponseHeader(String name)
		{
		return this.response.getLastHeader(name).getValue();
		}

	public String getResponseContent()
		{
		InputStream is = getStreamedResponseContent();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		try
			{
			String str;
			do
				{
				str = br.readLine();
				if(str!=null)
					sb.append(str + (sb.length()!=0?"\n":""));
				}
			while(str!=null);
			br.close();
			return sb.toString();
			}
		catch (IOException e)
			{
			e.printStackTrace();
			}
		return "";
		}

	public InputStream getStreamedResponseContent()
		{
		try
			{
			if (this.response.getEntity() != null)
				{
				return this.response.getEntity().getContent();
				}
			}
		catch (IOException e)
			{
			e.printStackTrace();
			}
		return null;
		}
	}