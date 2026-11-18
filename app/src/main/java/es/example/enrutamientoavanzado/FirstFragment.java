package es.example.enrutamientoavanzado;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import es.example.enrutamientoavanzado.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // (A) Inflar el layout con View Binding
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // (B) Evento: pulsar el botón
        binding.buttonFirst.setOnClickListener(v -> {
            String textoNota = binding.etInputNota.getText().toString().trim();

            // (C) Validación del título
            if (textoNota.isEmpty()) {
                Toast.makeText(getContext(), R.string.error_titulo_vacio,
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Ampliación 1: validación de la prioridad (1 a 3)
            int prioridad = leerPrioridad();
            if (prioridad < 1 || prioridad > 3) {
                Toast.makeText(getContext(), R.string.error_prioridad,
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // (D) SafeArgs: ruta + argumento obligatorio (tituloNota)
            //     prioridad tiene valor por defecto, por eso va con su setter
            FirstFragmentDirections.ActionFirstFragmentToSecondFragment action =
                    FirstFragmentDirections.actionFirstFragmentToSecondFragment(textoNota)
                            .setPrioridad(prioridad);

            // (E) Navegar
            NavHostFragment.findNavController(FirstFragment.this).navigate(action);
        });
    }

    private int leerPrioridad() {
        String texto = binding.etPrioridad.getText().toString().trim();
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // (F) Evitar fugas de memoria
    }
}
