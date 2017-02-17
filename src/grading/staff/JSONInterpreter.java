package grading.staff;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONInterpreter {

    JSONParser parser;

    public JSONInterpreter() {
        parser = new JSONParser();
    }

    /**
     * Parses a single response object
     * 
     * @param reader
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public Map<String,?> readResponseMap(BufferedReader reader) throws IOException, ParseException {

        Object o = readResponse(reader);
        if (o == null) {
            return null;
        }

        if (o instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String,?> map =  (Map<String,?>)o;
            return map;
        }

        throw new IOException("Expected a Map, got: " + o.toString());
    }
    
    /**
     * Parses a response
     * 
     * @param reader
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public Object readResponse(BufferedReader reader) throws IOException
    {
        String str = reader.readLine();
        
        if (str == null) {
            return null;
        }
        
        str = str.trim();
        
        if (!str.startsWith("{") && !str.startsWith("[")){
            System.err.println("Hmm... doesn't seem to be a JSON response.  Converting into one.");
            JSONObject response = new JSONObject();
            response.put("message", str);
            return response;
        }
        
        Object response = null;
        boolean parsed = false;
        while (!parsed) {
            try {
                response = parser.parse(str);
                parsed = true;  // could have successfully parsed a null response
            } catch (ParseException e) {
                // get next line
                String line = reader.readLine();
                if (line != null) {
                    str += line;
                } else {
                    throw new IOException("Failed to parse content: " + str);
                }
            }
        }
        
        return response;
    }
    
    /**
     * Parses a response
     * 
     * @param reader
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public Object readResponse(String str)
            throws IOException, ParseException {
        
        if (str == null) {
            return null;
        }
        
        str = str.trim();
        if (!str.startsWith("{") && !str.startsWith("[")){
            System.err.println("Hmm... doesn't seem to be a JSON response.  Converting into one.");
            JSONObject response = new JSONObject();
            response.put("message", str);
            return response;
        }
        
        return parser.parse(str);
    }

    /**
     * Parses an array of restaurants from a query
     * 
     * @param reader
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public List<?> readResponseList(BufferedReader reader)
            throws IOException, ParseException {

        Object o = readResponse(reader);
        if (o == null) {
            return null;
        }

        if (o instanceof List) {
            return (List<?>)o;
        }

        throw new IOException("Expected a Map, got: " + o.toString());
    }
    
    /**
     * Parses a single response object
     * 
     * @param str
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public Map<String,?> readResponseMap(String str) throws IOException, ParseException {

        Object o = readResponse(str);
        if (o == null) {
            return null;
        }

        if (o instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String,?> map =  (Map<String,?>)o;
            return map;
        }

        throw new IOException("Expected a Map, got: " + o.toString());
    }

    /**
     * Parses an array of restaurants from a query
     * 
     * @param str
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public List<?> readResponseList(String str)
            throws IOException, ParseException {

        Object o = readResponse(str);
        if (o == null) {
            return null;
        }

        if (o instanceof List) {
            return (List<?>)o;
        }

        throw new IOException("Expected a Map, got: " + o.toString());
    }
    
    public static String toJSON(Object obj, boolean pretty) {
        
        String out = null;
        if (obj instanceof JSONObject) {
            JSONObject jobj = (JSONObject)obj;
            out = jobj.toJSONString();
        } else if (obj instanceof JSONArray) {
            JSONArray jobj = (JSONArray)obj;
            out = jobj.toJSONString();
        } else if (obj instanceof Map) {
            JSONObject jobj = new JSONObject((Map<?,?>)obj);
            out = jobj.toJSONString();
        } else if (obj instanceof Collection) {
            JSONArray jobj = new JSONArray();
            jobj.addAll((Collection<?>)obj);
            out = jobj.toJSONString();
        } else {
            throw new IllegalArgumentException("input is not a known JSON type");
        }
        
        if (!pretty) {
            out = out.replaceAll("(\r|\n)", "");
        }
        return out;
    }

}
