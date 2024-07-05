package utalca.gestor_qr.MainModel;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.common.BitMatrix;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.core.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;

import utalca.gestor_qr.R;

public class QR_Generator {
    public Bitmap generateQRCode(String url, Context context) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            hints.put(EncodeHintType.MARGIN, 0); // Reduce el margen del QR
            BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 500, 500, hints); // Aumenta el tamaño del QR

            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            int myColor = ContextCompat.getColor(context, R.color.primary); // Obtiene el color desde los recursos

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? myColor : android.graphics.Color.WHITE); // Usa el color de los recursos
                }
            }
            return bmp;

        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
