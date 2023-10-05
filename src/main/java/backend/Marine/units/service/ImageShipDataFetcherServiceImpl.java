package backend.Marine.units.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

@Service
public class ImageShipDataFetcherServiceImpl implements ImageShipDataFetcherService {

	private static final String GOOGLE_API_KEY = "AIzaSyAMe2Lsp_78d1e6juctpFSJZINfWFr43rc";
	RestTemplate restTemplate = new RestTemplate();

	@Override
	public String getGoogleSearchEngineForImagePathOfQuery(String mmsi) {

		String url = "https://customsearch.googleapis.com/customsearch/v1?cx=9de8e229296fe6e53&q=mmsi " + mmsi + "&key="
				+ GOOGLE_API_KEY;

		@SuppressWarnings("deprecation")
		JSONParser parser = new JSONParser();
		JSONObject json;
		String url_img;
		try {
			String result = restTemplate.getForObject(url, String.class);
			json = (JSONObject) parser.parse(result);

			ObjectMapper mapper = new ObjectMapper();

			JSONObject[] res = mapper.readValue(json.getAsString("items"), JSONObject[].class);
			String as = res[1].getAsString("pagemap");
			String pre = "cse_image=[{src=";
			url_img = as.substring(as.indexOf(pre) + pre.length(), as.lastIndexOf("}]"));
		} catch (Exception e) {
			url_img = "https://localhost:443/ship/deflaut_ship.png";
		}
		return url_img;
	}

}
