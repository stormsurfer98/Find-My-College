package me.shreygupta.pullrequest;
import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PlaceholderFragment extends Fragment {
    private EditText editText01;
    private Button button01;
    Activity activity;

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my, container, false);
        editText01 = (EditText)rootView.findViewById(R.id.editText);
        button01 = (Button)rootView.findViewById(R.id.button);
        button01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String task = "https://inventory.data.gov/api/action/datastore_search?resource_id=38625c3d-5388-4c16-a30f-d105432553a4";
                task = task + "&q=" + editText01.getText().toString();
                task += "&limit=4";
                new RequestTask().execute(task);
            }
        });
        return rootView;
    }

    class RequestTask extends AsyncTask<String, String, String> { //http://stackoverflow.com/questions/3505930/make-an-http-request-with-android
        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    out.close();
                    responseString = out.toString();
                } else {
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
            try {
                JSONObject jsonObj = new JSONObject(result);
                String r = "";
                for(int i=0; i<jsonObj.getJSONObject("result").getJSONArray("records").length(); i++) {
                    r += "Name: ";
                    r += (String.valueOf(jsonObj.getJSONObject("result").getJSONArray("records").getJSONObject(i).getString("INSTNM")));
                    r += "\n";
                    r += "Location: ";
                    r += (String.valueOf(jsonObj.getJSONObject("result").getJSONArray("records").getJSONObject(i).getString("CITY")));
                    r += ", ";
                    r += (String.valueOf(jsonObj.getJSONObject("result").getJSONArray("records").getJSONObject(i).getString("STABBR")));
                    r += "\n\n";
                }
                try {
                    ((OnClickedListener) activity).displayData(r);
                }
                catch (ClassCastException cce) {
                }
            }
            catch(JSONException j) {
            }
        }
    }

    public interface OnClickedListener{
        public void displayData(String data);
    }

    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        this.activity = activity;
    }

    public static PlaceholderFragment newInstance() {
        PlaceholderFragment fragment = new PlaceholderFragment();
        return fragment;
    }
}