package br.cefetrj.sca.infra.moodle;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.springframework.stereotype.Component;

import br.cefetrj.sca.infra.IAuthDAO;

@Component
public class AuthDAOMoodle implements IAuthDAO {

	// moodle.org valid username and pass is teste_login, teste_login
	private String serviceURL = "http://localhost/moodle/login/auth.php";

	@Override
	public String getRemoteLoginResponse(String username, String password) {

		// RemoteLoginResponse response;
		String responseMessage = null;

		try {
			// from
			// https://docs.moodle.org/dev/Creating_a_web_service_client
			// https://github.com/moodlehq/sample-ws-clients/blob/master/JAVA-REST/RestJsonMoodleClient.java
			// https://moodle.org/mod/forum/discuss.php?d=215303
			// original author Jerome Mouneyrac jerome@moodle.com

			String urlParameters;

			urlParameters = "username=" + URLEncoder.encode(username, "UTF-8")
					+ "&password=" + URLEncoder.encode(password, "UTF-8")
					+ "&service="
					+ URLEncoder.encode("moodle_mobile_app", "UTF-8");

			HttpURLConnection con = (HttpURLConnection) new URL(serviceURL)
					.openConnection();

			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setDoInput(true);

			// Send Request
			{
				DataOutputStream wr = new DataOutputStream(
						con.getOutputStream());
				wr.writeBytes(urlParameters);
				wr.flush();
				wr.close();
			}

			// Get Response
			{
				InputStream is = con.getInputStream();
				BufferedReader rd = new BufferedReader(
						new InputStreamReader(is));
				String line;

				StringBuilder responseBuilder = new StringBuilder();
				while ((line = rd.readLine()) != null) {
					responseBuilder.append(line);
					responseBuilder.append('\r');
					responseMessage = responseBuilder.toString();
				}

				rd.close();
			}

			// ObjectMapper mapper = new ObjectMapper();
			// mapper.configure(
			// DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
			// false);
			//
			// try {
			// response = mapper.readValue(responseMessage.toString(),
			// RemoteLoginResponse.class);
			// } catch (Exception ex) {
			// response = new RemoteLoginResponse(null,
			// "Jackson mapper error.\nResponse message: "
			// + responseMessage + "\nError message: "
			// + ex.getMessage());
			// }

		} catch (Exception ex) {
			throw new RuntimeException("HTTP request error.\nError message: "
					+ ex.getMessage());
		}

		return responseMessage.toString();
	}
}