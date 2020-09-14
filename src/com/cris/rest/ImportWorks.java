package com.cris.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.um.dsi.gavea.orcid.client.OrcidAccessToken;
import org.um.dsi.gavea.orcid.client.exception.OrcidClientException;
import org.um.dsi.gavea.orcid.model.common.Country;
import org.um.dsi.gavea.orcid.model.common.CreditName;
import org.um.dsi.gavea.orcid.model.common.LanguageCode;
import org.um.dsi.gavea.orcid.model.common.OrcidId;
import org.um.dsi.gavea.orcid.model.common.SourceType;
import org.um.dsi.gavea.orcid.model.common.Subtitle;
import org.um.dsi.gavea.orcid.model.common.TranslatedTitle;
import org.um.dsi.gavea.orcid.model.common.Visibility;
import org.um.dsi.gavea.orcid.model.work.*;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import pt.ptcris.ORCIDClient;
import pt.ptcris.ORCIDClientImpl;
import pt.ptcris.PTCRISync;



@Path("/import")
public class ImportWorks {
	private static final String orcid_login_uri = "https://orcid.org/"; // "https://sandbox.orcid.org/";
	private static final String orcid_api_uri = "https://pub.orcid.org/";
	private static final String orcid_redirect_uri = "https://developers.google.com/oauthplayground";

	private static final String cris_client_id = "APP-BWDZBPVHWP21TFOQ"; // meu
	private static final String cris_client_secret = "b66c4d62-c4ec-4b4c-bc8f-fd684d2ff6aa";// meu
	
	@Path("{f}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWork(@PathParam("f") String f) throws IllegalArgumentException, OrcidClientException, InterruptedException, JsonProcessingException {
		Tester proggress = new Tester();
		OrcidAccessToken orcidToken = new OrcidAccessToken();
		orcidToken.setAccess_token("b755ea1d-21ba-4306-90a7-8377d9b72b27");

		//orcidToken.setOrcid("0000-0002-0417-9402");
		orcidToken.setOrcid(f);

		ORCIDClient crisClient = new ORCIDClientImpl(orcid_login_uri, orcid_api_uri, cris_client_id, cris_client_secret,
				orcid_redirect_uri, orcidToken);

		List<Work> locals = new ArrayList<Work>();

		List<Work> pubs = PTCRISync.importWorks(crisClient, locals, proggress);
		
		pubs.add(addWorkCamps());
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		//mapper.setDateFormat(DateFormat.);
		String jsonString = mapper.writeValueAsString(pubs);
		return Response.ok()
	               .entity(jsonString)
	               .header("Access-Control-Allow-Origin", "*")
	               .build();
	}
	
	@POST
	@Path("/works/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDifWorks(String w, @PathParam("id") String id) throws IllegalArgumentException, OrcidClientException, InterruptedException, JsonParseException, JsonMappingException, IOException{
		Tester proggress = new Tester();
		OrcidAccessToken orcidToken = new OrcidAccessToken();
		orcidToken.setAccess_token("b755ea1d-21ba-4306-90a7-8377d9b72b27");

		//orcidToken.setOrcid("0000-0002-0417-9402");
		orcidToken.setOrcid(id);

		ORCIDClient crisClient = new ORCIDClientImpl(orcid_login_uri, orcid_api_uri, cris_client_id, cris_client_secret,
				orcid_redirect_uri, orcidToken);

		/*List<Work> locals = new ArrayList<Work>();
		Work work = addWorkCamps();
		locals.add(w);*/
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		List<Work> wa = mapper.readValue(w,new TypeReference<List<Work>>(){});

		List<Work> pubs = PTCRISync.importWorks(crisClient, wa, proggress);
		String jsonString = mapper.writeValueAsString(pubs);
		return Response.ok()
	               .entity(jsonString)
	               .header("Access-Control-Allow-Origin", "*")
	               .build();
	}
	
	private Work addWorkCamps() {
		Work work = new Work();
		WorkTitle title = new WorkTitle();
		org.um.dsi.gavea.orcid.model.common.Subtitle subtitle = new Subtitle();
		subtitle.setContent("subtitle");
		TranslatedTitle transtitle = new TranslatedTitle("translatedTitle", LanguageCode.PT);
		title.setTitle("titulo");
		title.setSubtitle(subtitle);
		title.setTranslatedTitle(transtitle);
		work.setTitle(title);
		
		work.setShortDescription("shortDescription");
		
		work.setCountry(new Country("pt", Visibility.PUBLIC));
		
		Contributor autor = new Contributor();
		autor.setContributorOrcid(new OrcidId("uri", "uriPath", "path", "host"));
		autor.setCreditName(new CreditName("nome"));
		autor.setContributorEmail(new ContributorEmail("email@email.com"));
		ContributorAttributes atributos = new ContributorAttributes();
		atributos.setContributorRole(ContributorRole.AUTHOR);
		atributos.setContributorSequence(ContributorSequence.FIRST);
		autor.setContributorAttributes(atributos);
		List<Contributor> l = new ArrayList<Contributor>();
		l.add(autor);
		WorkContributors wautors = new WorkContributors();
		wautors.setContributor(l);
		work.setContributors(wautors);
		return work;
	}
}
