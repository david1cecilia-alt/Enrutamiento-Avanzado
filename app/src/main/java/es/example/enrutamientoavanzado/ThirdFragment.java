package es.example.enrutamientoavanzado;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import es.example.enrutamientoavanzado.databinding.FragmentThirdBinding;

/** Ampliación 3: pantalla de confirmación. */
public class ThirdFragment extends Fragment {

    private FragmentThirdBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentThirdBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            ThirdFragmentArgs args = ThirdFragmentArgs.fromBundle(getArguments());
            binding.textviewConfirmacion.setText(
                    getString(R.string.confirmacion_formato, args.getTituloNota()));
        }

        // Acción con popUpTo=FirstFragment (inclusive): la pila queda solo con la pantalla 1
        binding.buttonInicio.setOnClickListener(v ->
                NavHostFragment.findNavController(ThirdFragment.this)
                        .navigate(ThirdFragmentDirections.actionThirdFragmentToFirstFragment()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
