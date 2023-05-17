package br.com.dlweb.maternidade.mae;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import br.com.dlweb.maternidade.R;

public class AdicionarFragment extends Fragment {

    private EditText etNome;
    private EditText etTelefone;
    private EditText etCelular;
    private EditText etComercial;
    private FirebaseFirestore db;

    public AdicionarFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.mae_fragment_adicionar, container, false);

        etNome = v.findViewById(R.id.editTextNome);
        etTelefone = v.findViewById(R.id.editTextCep);
        etCelular = v.findViewById(R.id.editTextCelular);
        etComercial= v.findViewById(R.id.editTextComercial);

        db = FirebaseFirestore.getInstance();
        Button btnSalvar = v.findViewById(R.id.buttonAdicionar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionar();
            }
        });

        return v;



    }
    private void adicionar () {
        if (etNome.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe o nome!", Toast.LENGTH_LONG).show();
        } else if (etTelefone.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe o Telefone!", Toast.LENGTH_LONG).show();
        }else if (etCelular.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe o Celular!", Toast.LENGTH_LONG).show();
        } else if (etComercial.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe o numero Comercial!", Toast.LENGTH_LONG).show();
        }  else {
            Mae m = new Mae();
            m.setNome(etNome.getText().toString());
            m.setCep(etTelefone.getText().toString());
            m.setCelular(etCelular.getText().toString());
            m.setComercial(etComercial.getText().toString());

            CollectionReference collectionMae = db.collection("Contatos");
            collectionMae.add(m).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(getActivity(), "Contato salva!", Toast.LENGTH_LONG).show();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameMae, new ListarFragment()).commit();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(getActivity(), "Erro ao salvar a Contato!", Toast.LENGTH_LONG).show();
                    Log.d("AdicionarContato", "mensagem de erro: ", e);
                }
            });
        }
    }

}