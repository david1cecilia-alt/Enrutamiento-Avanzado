package es.example.enrutamientoavanzado;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import es.example.enrutamientoavanzado.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() == null) {
            return;
        }

        // (A) Recibir los argumentos con SafeArgs
        SecondFragmentArgs args = SecondFragmentArgs.fromBundle(getArguments());
        String titulo = args.getTituloNota();
        binding.textviewSecond.setText(titulo);

        // Ampliación 1: prioridad (0 = no se ha indicado, p. ej. al llegar por deep link)
        int prioridad = args.getPrioridad();
        if (prioridad > 0) {
            binding.textviewPrioridad.setText(getString(R.string.prioridad_formato, prioridad));
            binding.textviewPrioridad.setVisibility(View.VISIBLE);
        } else {
            binding.textviewPrioridad.setVisibility(View.GONE);
        }

        // (B) Volver: sacar esta pantalla de la pila
        binding.buttonSecond.setOnClickListener(v ->
                NavHostFragment.findNavController(SecondFragment.this)
                        .popBackStack());

        // Ampliación 3: ir a la pantalla de confirmación pasando el título
        binding.buttonConfirmar.setOnClickListener(v ->
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(SecondFragmentDirections
                                .actionSecondFragmentToThirdFragment(titulo)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
