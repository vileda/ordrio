package ordrio;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.Vertx;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class CreateOrdrCommandResourceTest {

	private static final String BASE_URL = "http://localhost:8080";

	@BeforeClass
	public static void beforeClass() throws InterruptedException {
		new OrdrioMain().run(Vertx.vertx());
		Thread.sleep(1000);
	}

	@Test
	public void testCreateOrdr() throws Exception {
		String name = "foobar123";
		HttpResponse execute = postCreateOrdr(name);
		assertPostCreateOrdr(execute, name);
	}

	@Test
	public void testCreateItem() throws Exception {
		String name = "foobar123";
		HttpResponse execute = postCreateOrdr(name);
		JsonObject jsonObject = new JsonObject(IOUtils.toString(execute.getEntity().getContent()));
		String ordrLink = jsonObject.getJsonArray("links").getJsonObject(1).getString("href");
		List<NameValuePair> params = new ArrayList<>(Arrays.asList(
				new BasicNameValuePair("name", "testitem1"),
				new BasicNameValuePair("ordrId", jsonObject.getString("id"))
		));
		execute = post(ordrLink, params);
		jsonObject = new JsonObject(IOUtils.toString(execute.getEntity().getContent()));
		assertThat(execute.getStatusLine().getStatusCode(), is(200));
		String ordrAggregateLink = jsonObject.getJsonArray("links")
				.getJsonObject(0).getString("href");
		execute = get(ordrAggregateLink);
		jsonObject = new JsonObject(IOUtils.toString(execute.getEntity().getContent()));
		JsonArray items = jsonObject.getJsonArray("items");
		assertThat(items.size(), is(1));
		JsonObject item = items.getJsonObject(0);
		assertThat(item.getString("name"), is("testitem1"));
	}

	private void assertPostCreateOrdr(HttpResponse execute, String name) throws IOException {
		assertThat(execute.getStatusLine().getStatusCode(), is(200));
		JsonObject jsonObject = new JsonObject(IOUtils.toString(execute.getEntity().getContent()));
		String id = jsonObject.getString("id");
		assertNotNull(id);
		assertThat(jsonObject.getString("name"), is(name));
		JsonArray links = jsonObject.getJsonArray("links");
		assertThat(links.size(), is(2));
		assertThat(links.getJsonObject(0).getString("rel"), is("self"));
		assertThat(links.getJsonObject(0).getString("href"), containsString(id));
		assertThat(links.getJsonObject(0).getString("method"), is("GET"));

		assertThat(links.getJsonObject(1).getString("rel"), is("add"));
		assertThat(links.getJsonObject(1).getString("href"), containsString(id));
		assertThat(links.getJsonObject(1).getString("method"), is("POST"));
	}

	private HttpResponse postCreateOrdr(String name) throws IOException {
		List<NameValuePair> parameters = new ArrayList<>();
		parameters.add(new BasicNameValuePair("name", name));
		return post("/api/ordr", parameters);
	}

	@Test
	public void testGetOrdrAggregate() throws Exception {
		String name = UUID.randomUUID().toString();
		JsonObject result = new JsonObject(IOUtils.toString(postCreateOrdr(name).getEntity().getContent()));

		HttpResponse execute = get("/api/aggregate/ordr/" + result.getString("id"));
		String actual = IOUtils.toString(execute.getEntity().getContent());
		assertThat(actual, execute.getStatusLine().getStatusCode(), is(200));
		final JsonObject jsonObject = new JsonObject(actual);
		assertNotNull(jsonObject.getString("id"));
		assertThat(actual, jsonObject.getString("name"), containsString(name));
	}

	private HttpResponse post(String path, List<NameValuePair> params) throws IOException {
		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost(BASE_URL + path);
		httpPost.setEntity(new UrlEncodedFormEntity(params));
		return httpclient.execute(httpPost);
	}

	private HttpResponse get(String path) throws IOException {
		HttpClient httpclient = HttpClientBuilder.create().build();
		String uri = BASE_URL + path;
		HttpGet httpGet = new HttpGet(uri);
		return httpclient.execute(httpGet);
	}
}