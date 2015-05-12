package carranza.com.uattendance;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AttendanceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AttendanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AttendanceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Button openAttendanceButton;
    private Button setAttendanceButton;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AttendanceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AttendanceFragment newInstance(String param1, String param2) {
        AttendanceFragment fragment = new AttendanceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static AttendanceFragment newInstance() {
        AttendanceFragment fragment = new AttendanceFragment();

        return fragment;
    }

    public AttendanceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        new GetDocumentsAsyncTask().execute(Utils.getHost() + "api/document");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_attendance, container, false);

        openAttendanceButton = (Button) view.findViewById(R.id.action_open_attendance_button);
        openAttendanceButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new GetSubjectsAsyncTask().execute(Utils.getHost() + "api/subject");
            }
        });

        setAttendanceButton = (Button) view.findViewById(R.id.action_set_attendance_button);
        setAttendanceButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new SetAttendanceTask().execute(Utils.getHost() + "api/annotation");
                Utils.flag = 1;
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(1);

        /*
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private class SetAttendanceTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = null;
            HttpPost request = null;
            HttpResponse httpResponse = null;
            InputStream inputStream = null;
            String result = "";

            try {
                httpClient = new DefaultHttpClient();

                request = new HttpPost(params[0]);
                // request.addHeader("X_UPI_PASSWORD", UserModel.getUser().getPassword());
                // request.addHeader("X_UPI_USERNAME", UserModel.getUser().getEmail());
                request.addHeader("X_PASSWORD", "caracola");
                request.addHeader("X_USERNAME", "carranzafr@gmail.com");

                httpClient.execute(request);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getView().getContext(), "Annotation Successful!", Toast.LENGTH_SHORT).show();
        }
    }

    private class GetSubjectsAsyncTask extends AsyncTask<String, Void, String> {

        private JSONArray subjects;
        private String mSelectedSubject;
        private ArrayAdapter<String> s = new ArrayAdapter<String>(
                getView().getContext(),
                android.R.layout.select_dialog_singlechoice);

        @Override
        protected String doInBackground(String... params) {
            return Utils.requestGet(params[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                subjects = new JSONArray(result);
                for (int i = 0; i < subjects.length(); i++) {
                    JSONObject subject = subjects.getJSONObject(i);

                    s.add(subject.getString("iniciales"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(getView().getContext());
            builder.setTitle(R.string.title_dialog_select_subject)
                    .setSingleChoiceItems(s, -1,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mSelectedSubject = s.getItem(which);
                                }
                            })
                    .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    })
                    .setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            new CreateAttendanceAsyncTask().execute(Utils.getHost() + "api/attendance", mSelectedSubject);
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private class CreateAttendanceAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("end", "30"));
            nameValuePairs.add(new BasicNameValuePair("tipo", "T"));
            nameValuePairs.add(new BasicNameValuePair("iniciales", params[1]));
            nameValuePairs.add(new BasicNameValuePair("bMAC", "00:15:83:0C:BF:EC"));

            return Utils.requestPost(params[0], nameValuePairs);
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getView().getContext(), "Attendance Open!", Toast.LENGTH_SHORT).show();
        }
    }

    /* ------------------------------------------------------------------------------------------ */

    private class GetDocumentsAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return Utils.requestGet(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                Utils.documents = new JSONArray(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
