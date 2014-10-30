package me.shreygupta.pullrequest;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class NextFragment extends Fragment {
    private Button button01;
    private TextView textView01;
    private String myData;
    Activity activity;

    public NextFragment() {
        myData = "No results to show!";
    }

    public NextFragment(String data) {
        if(data.equals(""))
            myData = "No results found!";
        else
            myData = data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view, container, false);
        button01 = (Button)rootView.findViewById(R.id.buttonN);
        textView01 = (TextView)rootView.findViewById(R.id.textViewN);
        textView01.setText(myData);
        button01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ((OnClickedListener) activity).goBack();
                }
                catch (ClassCastException cce) {
                }
            }
        });
        return rootView;
    }

    public interface OnClickedListener {
        public void goBack();
    }

    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        this.activity = activity;
    }

    public static NextFragment newInstance(String data) {
        NextFragment fragment = new NextFragment(data);
        return fragment;
    }
}