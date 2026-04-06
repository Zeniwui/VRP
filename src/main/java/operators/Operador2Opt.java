package operators;

import java.util.ArrayList;
import java.util.List;

public class Operador2Opt implements OperadorLocalIntraRuta{

    @Override
    public List<Integer> aplicar(List<Integer> segmento, int v1, int v2) {
        List<Integer> nuevoSegmento = new ArrayList<>(segmento);
        for (int i = v1 + 1, j = v2; i < j; i++, j--){
            int aux = nuevoSegmento.get(i);
            nuevoSegmento.set(i, nuevoSegmento.get(j));
            nuevoSegmento.set(j, aux);
        }
        return nuevoSegmento;
    }
}
