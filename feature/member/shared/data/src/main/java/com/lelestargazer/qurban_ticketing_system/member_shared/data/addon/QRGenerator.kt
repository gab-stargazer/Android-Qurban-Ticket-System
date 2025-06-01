package com.lelestargazer.qurban_ticketing_system.member_shared.data.addon

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.Color.convertRgbToCmyk
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.SolidBorder
import com.itextpdf.layout.element.AreaBreak
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.element.Text
import com.itextpdf.layout.properties.HorizontalAlignment
import com.itextpdf.layout.properties.TextAlignment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import qrcode.QRCode
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Calendar

class QRGenerator {

    suspend fun saveCoupons(qrDataList: List<QRGeneratorData>) {
        withContext(Dispatchers.IO) {
            val documentsDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            val file = File(documentsDir, "kupon-qurban-${currentYear}.pdf")
            val writer = PdfWriter(file)
            val pdf = PdfDocument(writer)
            val document = Document(pdf, PageSize.A4)
            document.setMargins(21F, 17.5F, 21F, 17.5F)

            // Process in smaller batches (8 instead of 16) to reduce memory usage
            qrDataList.chunked(16).forEach { chunk ->
                val table = Table(4)
                chunk.forEachIndexed { index, qrData ->
                    // Generate smaller QR code (size 25 instead of 50)
                    val qrCode = QRCode.ofRoundedSquares()
                        .withSize(25)
                        .build(qrData.qrCode)
                        .render()

                    // Convert to compressed bitmap
                    val bitmap = BitmapFactory.decodeByteArray(
                        qrCode.getBytes(),
                        0,
                        qrCode.getBytes().size
                    )
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 80, stream) // Compress to 80% quality
                    val compressedBytes = stream.toByteArray()

                    // Create image data from compressed bytes
                    val imageData = ImageDataFactory.create(compressedBytes)
                    val image = Image(imageData)
                        .scaleToFit(100F, 100F) // Reduced from 120F to 100F
                        .setMargins(10F, 10F, 5F, 10F)

                    // Clean up
                    bitmap.recycle()
                    stream.close()

                    val couponStatus = Text(qrData.couponStatus + "\n")
                        .setFontSize(10F)
                        .setBold()

                    val paragraph = Paragraph()
                        .add(image)
                        .add(couponStatus)
                        .add(qrData.couponName)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFontSize(12F)

                    val cell = Cell()
                        .setPadding(0F)
                        .setWidth(140F)
                        .setHeight(190F)
                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                        .setBorder(SolidBorder(convertRgbToCmyk(DeviceRgb(0f, 0f, 0f)), 1f))

                    cell.add(paragraph)
                    table.addCell(cell)
                }

                document.add(table)
                if (chunk != qrDataList.chunked(16).last()) {
                    document.add(AreaBreak())
                }
            }

            document.close()
            pdf.close()
            writer.close()
        }
    }

    fun getQrImage(qrHash: String): Bitmap {
        val qrCode = QRCode.ofRoundedSquares()
            .withSize(25) // Reduced from 50
            .build(qrHash)
            .render()

        val bitmap = BitmapFactory.decodeByteArray(
            qrCode.getBytes(),
            0,
            qrCode.getBytes().size
        )
        return bitmap
    }

    data class QRGeneratorData(
        val qrCode: String,
        val couponStatus: String,
        val couponName: String,
    )
}