package pl.gov.cmp.utils;

import java.util.List;

public class JpaUtils {

    public static boolean sprawdzListeSortowania(List<String> listaDoSortowania, String wartosc) {
        return listaDoSortowania.contains(wartosc);
    }
}
