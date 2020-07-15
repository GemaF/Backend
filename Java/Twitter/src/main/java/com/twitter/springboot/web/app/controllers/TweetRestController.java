package com.twitter.springboot.web.app.controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.twitter.springboot.web.app.models.entity.Tweet;
import com.twitter.springboot.web.app.models.services.ITweetService;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;


@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class TweetRestController {
	
	Tweet tweet = new Tweet();
	
	public void execute() throws TwitterException {	
		
		StatusListener listener = new StatusListener() {
	    	
	    	int numberOfTweets = 20;
			int cont = 0;
			boolean incremento = false;
			
			public void onStatus(Status status) { 	
	
				int contTotalTags = 0;
				
				int seguidores = status.getUser().getFollowersCount();
	        	String lang = status.getLang();
	 
	        	long userid = status.getId();
	        	String nombre = status.getUser().getName();
	        	String texto = status.getText();
	        	String location = status.getUser().getLocation();
	        	boolean validado = status.getUser().isVerified();
	        	
	        	// consultarHashtags(texto);
		        	
		        
	        	Pattern pattern = Pattern.compile("(#[A-Za-z0-9-_]+)(?:#[A-Za-z0-9-_]+)*");
	    		Matcher matcher = pattern.matcher(status.getText());
	    		
	    		String totalMatcher = null;
	    		
	    		while (matcher.find()){
	    			
	    			totalMatcher += matcher.group(1);
	    			
	    			if (matcher.group(1) == matcher.group(1)+1) {
	    				contTotalTags++;
	    			}
	    		} 
	    		

	    		Writer out;  
        		
	        	try {
	        		
	        		if ((seguidores >= 5000) && (lang.equals("es") || lang.equals("fr") || lang.equals("it")    )) { 
 
		        			File file = new File("C:\\Gema\\Tweets\\Twitter\\src\\main\\resources\\import.sql");
	        				
	        				out = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(file, true), "utf-8")); 
	        		
	    					while (cont <= numberOfTweets && incremento == false) {
	    						
	    						if (cont <= numberOfTweets ) {
	    							out.write("INSERT INTO tweets (id, usuario, texto, localizacion, validacion) VALUES (" + userid + ", \"" + nombre + "\", \"" + texto + "\", \"" + location + "\", " + validado + ");" + "\n");
	    							
	    							incremento = true; 
	    							out.close();
	    							
	    						} else {
	    							incremento = false;
	    						}
		        				cont++;
	    				    }
		        	}
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
	        

	        
	        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
	        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
	        public void onException(Exception ex) {
	            ex.printStackTrace();
	        }
	       
			public void onScrubGeo(long userId, long upToStatusId) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onStallWarning(StallWarning warning) {
				// TODO Auto-generated method stub
			}
	    };
	    
	   TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
	   twitterStream.addListener(listener);
	   twitterStream.sample();
	   
	}



	@Autowired
	private ITweetService tweetService;

	
	@GetMapping("/tweets")
	public List<Tweet> index() {
		return tweetService.findAll();
	}
	
	
	@GetMapping("/tweets/{id}")
	public Tweet show (@PathVariable Long id) {

		return this.tweetService.findById(id);
	}
	
 
	@PutMapping("/tweets/{id}")
	@ResponseStatus (HttpStatus.CREATED)
	public Tweet marcarTweetValidado(@RequestBody Tweet tweet, @PathVariable Long id) {
		
		Tweet tweetActual = this.tweetService.findById(id);

		if (tweetActual.getTexto().endsWith("a")) {
			tweetActual.setValidacion(true);
		}
		
		this.tweetService.save(tweetActual);
		
		return tweetActual;
	}
	

	
	@GetMapping("/tweetsValidados")
	public List<Tweet> consultarTweetValidados() {
		
		List<Tweet> listaTweets = this.tweetService.findAll();
		List<Tweet> listaValidados = new ArrayList<Tweet>();
		
		
		for ( Tweet tweetsValidados : listaTweets ) {
			
			if ( tweetsValidados.isValidacion() ==  true  ) {
				listaValidados.add(tweetsValidados);
			}
		}
		
		return listaValidados;
	}	
	
	
	
	public <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map ) {
		
	    List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
	    
	    Collections.sort( list, new Comparator<Map.Entry<K, V>>() {
	    	public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
	    		// Ascendente:
	    		// return (o1.getValue()).compareTo( o2.getValue() );
	    		// Descendente:
	    		return (o2.getValue()).compareTo( o1.getValue() );
	        }
	    });
	
	    Map<K, V> result = new LinkedHashMap<K, V>();
	    for (Map.Entry<K, V> entry : list){
	        result.put( entry.getKey(), entry.getValue() );
	    }
	    return result;
	}
	
	
	

	@GetMapping("/hashtags")
	public void consultarHashtags(String texto) {
		
		
		Pattern pattern = Pattern.compile("(#[A-Za-z0-9-_]+)(?:#[A-Za-z0-9-_]+)*");
		Matcher matcher = pattern.matcher(texto);
		
		String hashtagTExto = "";
		ArrayList<String> lstTag = new ArrayList<String>();
	
		while (matcher.find()) {
			
			hashtagTExto = matcher.group(1);
			lstTag.add(hashtagTExto);		
	    }

        
		HashMap <String, Integer> datos = new HashMap <String, Integer>();
        
        int contador = 0;
        String minus = "";
        
        for (String palabra : lstTag) {
        	
            minus = palabra.toLowerCase();
            contador = 0;
            
            if(!datos.containsValue(minus)) {
            	
               for (String demas : lstTag) {
                   if(demas.toLowerCase().equals(minus)){
                       contador++;
                   }
               }
               
               datos.put(minus, contador);
               // System.out.println(minus + ": " + contador + ((contador == 1)?" vez":" veces"));
            }
        }
     

        Map valoreOrdenados = sortByValue(datos);
        Iterator it = valoreOrdenados.entrySet().iterator();
        Map.Entry entry = null;

        boolean diezElementos = false;
        
        while (it.hasNext()) {

        	 for (int i = 10; i >= 0; i--) {
				
        		if ((i > 0) && (diezElementos == false)) {
        			
        			entry = (Map.Entry)it.next();
        			
        			System.out.println("HashTag  " + i + " : " + entry.getKey() + " => " + entry.getValue());
	        		
	        		it.remove(); 

        		} else {
        			diezElementos = true;
        		}
        	}
        }
	}
}
