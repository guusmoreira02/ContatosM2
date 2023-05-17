package br.com.dlweb.maternidade.mae;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import br.com.dlweb.maternidade.R;

public class EditarFragment extends Fragment {

    private EditText etNome;
    private EditText etTelefone;
    private EditText etCelular;
    private EditText etComercial;
    private Mae m;
    private FirebaseFirestore db;


    public EditarFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.mae_fragment_editar, container, false);

        etNome = v.findViewById(R.id.editTextNome);
        etTelefone = v.findViewById(R.id.editTextCep);
        etCelular = v.findViewById(R.id.editTextCelular);
        etComercial= v.findViewById(R.id.editTextComercial);

        db = FirebaseFirestore.getInstance();
        Bundle b = getArguments();
        String id_mae = b != null ? b.getString("id") : null;

        assert id_mae != null;
        DocumentReference documentMae = db.collection("Contatos").document(id_mae);
        documentMae.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete( Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        m = task.getResult().toObject(Mae.class);
                        assert m != null;
                        etNome.setText(m.getNome());
                        etTelefone.setText(m.getCep());
                        etCelular.setText(m.getCelular());
                        etComercial.setText(m.getComercial());
                    }else {
                        Toast.makeText(getActivity(), "Erro ao buscar a Contato!", Toast.LENGTH_LONG).show();
                        Log.d("EditarContato", "nenhum Contato encontrado");
                    }
                } else {
                    Toast.makeText(getActivity(), "Erro ao buscar Contato!", Toast.LENGTH_LONG).show();
                    Log.d("EditarContato", "erro: ", task.getException());
                }
            }
        });

        Button btnEditar = v.findViewById(R.id.buttonEditar);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar(id_mae);
            }
        });

        Button btnExcluir = v.findViewById(R.id.buttonExcluir);
        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.dialog_excluir_mae);
                builder.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        excluir(id_mae);
                    }
                });
                builder.setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
// Não faz nada
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


        return v;
    }

    private void editar (String id) {
        if (etNome.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe o nome!", Toast.LENGTH_LONG).show();
        }else if (etTelefone.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe o Telefone!", Toast.LENGTH_LONG).show();
        } else if (etCelular.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe o Celular!", Toast.LENGTH_LONG).show();
        } else if (etComercial.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Por favor, informe o numero Comercial!", Toast.LENGTH_LONG).show();
        }else {
            m.setNome(etNome.getText().toString());
            m.setCep(etTelefone.getText().toString());
            m.setCelular(etCelular.getText().toString());
            m.setComercial(etComercial.getText().toString());
            DocumentReference documentMae = db.collection("Contatos").document(id);
            documentMae.set(m).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Contato atualizado!", Toast.LENGTH_LONG).show();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameMae, new ListarFragment()).commit();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Log.w("Editar Contato", "erro: ", e);
                        }
                    });
        }
    }

    private void excluir(String id) {
        db.collection("Contatos").document(id)
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Contato excluído!", Toast.LENGTH_LONG).show();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameMae, new ListarFragment()).commit();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.w("ExcluirContato", "erro: ", e);
                    }
                });
    }

}