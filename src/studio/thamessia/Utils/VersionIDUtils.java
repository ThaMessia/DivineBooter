package studio.thamessia.Utils;

public class VersionIDUtils {

    public static int getClientBoundKeepAliveID(int pv) {
        return pv >= 47 && pv < 67 ? 0x00 : pv >= 67 && pv < 80 ? 0x1F : pv >= 80 && pv < 86 ? 0x20 : pv >= 86 && pv < 318 ? 0x1F : pv >= 318 && pv < 332 ? 0x20 :
                pv >= 332 && pv < 345 ? 0x1F : pv >= 345 && pv < 389 ? 0x20 : pv >= 389 && pv < 471 ?
                        0x21 : pv >= 471 && pv < 550 ? 0x20 : 0x21;
    }

    public static int getServerBoundKeepAliveID(int pv) {
        return pv >= 47 && pv < 67 ? 0x00 : pv >= 67 && pv < 80 ? 0x0A : pv >= 80 && pv < 318 ? 0x0B : pv >= 318 && pv < 332 ? 0x0B :
                pv >= 332 && pv < 336 ? 0x0C : pv >= 336 && pv < 343 ? 0x0B : pv >= 343 && pv < 386 ? 0x0B : pv >= 386 && pv < 389 ? 0x0C :
                        pv >= 389 && pv < 464 ? 0x0E : pv >= 464 && pv < 471 ? 0x10 : 0x0F;
    }

    //CODICE ALIENO COSI' NON SKIDDATE
}