package backend.Marine.units.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

@Service
public class ImageShipDataFetcherServiceImpl implements ImageShipDataFetcherService {

	private static final String GOGLE_API_KEY = "AIzaSyAMe2Lsp_78d1e6juctpFSJZINfWFr43rc";

	@Override
	public String getGoogleSearchEngineForImagePathOfQuery(String mmsi) {

		String url = "https://customsearch.googleapis.com/customsearch/v1?cx=9de8e229296fe6e53&q=mmsi " + mmsi + "&key="
				+ GOGLE_API_KEY;
		RestTemplate restTemplate = new RestTemplate();

		@SuppressWarnings("deprecation")
		JSONParser parser = new JSONParser();
		JSONObject json = null;
		String url_img = "";
		try {
			String result = restTemplate.getForObject(url, String.class);
			json = (JSONObject) parser.parse(result);

			ObjectMapper mapper = new ObjectMapper();

			JSONObject[] res = mapper.readValue(json.getAsString("items"), JSONObject[].class);
			String as = res[1].getAsString("pagemap").toString();
			String pre = "cse_image=[{src=";
			url_img = as.substring(as.indexOf(pre) + pre.length(), as.lastIndexOf("}]"));
		} catch (Exception e) {
			url_img = "https://localhost:443/ship/deflaut_ship.png";
		}
		return url_img;
	}

}
