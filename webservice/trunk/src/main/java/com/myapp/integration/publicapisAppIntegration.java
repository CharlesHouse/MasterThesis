package com.myapp.integration;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.SimpleTimer;
import io.prometheus.client.Summary;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.List;


import java.net.URL;
import java.net.HttpURLConnection;

@Path( "/metrics" )
public class publicapisAppIntegration
{

    /*static final Counter requests = Counter.build()
      .name( "hello_worlds_total" )
      .help( "Number of hello worlds served." ).register();*/

    private static final String HASH_FUNCTION = "SHA-512";

    @GET
    @Path( "/" )
    @Produces( MediaType.TEXT_PLAIN )
    public String Metrics()
    {
        StringWriter writer = new StringWriter();
        try
        {
            io.prometheus.client.exporter.common.TextFormat.write004(
              writer, CollectorRegistry.defaultRegistry.metricFamilySamples() );
        }
        catch( Exception e )
        {
            return e.getMessage();
        }
        return writer.toString();
    }

    @GET
    @Path( "/v1.0/{category}" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON + ";charset=utf-8" )
    public Response getCategoty( @PathParam( "category" ) String category )
    {
        Metrics.requestsTotal.inc();

        String payload = "{\"test\":\"test\"}";
        String requestUrl = "https://api.publicapis.org/entries?category=" + category + "&https=true";
        String jsonString;
        String hashOutput;
        try
        {
            Summary.Timer timer = Metrics.requestLatency.startTimer();
            //Summary.Timer timer = new Summary.Timer(this, SimpleTimer.defaultTimeProvider.nanoTime() );
            jsonString = doHttpUrlConnectionAction( requestUrl );
            hashOutput = Hasher.hash( jsonString, HASH_FUNCTION );
            timer.observeDuration();
        }
        catch( Exception e )
        {
            Metrics.requestFailures.inc();
            System.out.println( "Show Exception: " + e.getMessage() );
            throw new RuntimeException( e.getMessage() );
        }

        String result = "{ \"status_code\" : \"" + "200" + "\", \"result\" : "
          + hashOutput + " }";

        return Response.status( Response.Status.OK ).entity( result ).build();
    }

    private String doHttpUrlConnectionAction( String desiredUrl )
      throws Exception
    {
        URL url = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder;

        try
        {
            url = new URL( desiredUrl );
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod( "GET" );
            connection.setReadTimeout( 15 * 1000 );
            connection.connect();
            reader = new BufferedReader( new InputStreamReader( connection.getInputStream() ) );
            stringBuilder = new StringBuilder();
            String line = null;
            while( ( line = reader.readLine() ) != null )
            {
                stringBuilder.append( line + "\n" );
            }
            return stringBuilder.toString();
        }
        catch( Exception e )
        {
            e.printStackTrace();
            throw e;
        }
        finally
        {
            if( reader != null )
            {
                try
                {
                    reader.close();
                }
                catch( IOException ioe )
                {
                    ioe.printStackTrace();
                }
            }
        }
    }
}