package com.lelestargazer.qurban_ticketing_system.member_shared.data.addon

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import com.itextpdf.io.font.constants.StandardFonts
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.Border
import com.itextpdf.layout.element.AreaBreak
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.element.Text
import com.itextpdf.layout.properties.HorizontalAlignment
import com.itextpdf.layout.properties.VerticalAlignment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single
import qrcode.QRCode
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Calendar

@Single
class QRGenerator {

    suspend fun saveCoupons(
        //TODO: Change into received param only later without default value
        location: String = "Lokasi Test",
        time: String = "Jumat, 19 Agustus 2023",
        qrDataList: List<QRGeneratorData>
    ) {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        withContext(Dispatchers.IO) {
            val documentsDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            val file =
                File(documentsDir, "kupon-qurban-${currentYear}.pdf")
            val writer = PdfWriter(file)
            val pdf = PdfDocument(writer)
            val document = Document(pdf, PageSize.A4)
            document.setMargins(0F, 0F, 0F, 0F)


            // Process in smaller batches (8 instead of 16) to reduce memory usage
            qrDataList.chunked(CHUNK_SIZE).forEach { chunk ->
                val table = Table(floatArrayOf(50f, 50f))
                    .setAutoLayout()
                    .useAllAvailableWidth()

                chunk.forEach { qrData ->
                    // Generate QR code
                    val qrCode = QRCode.ofRoundedSquares()
                        .withSize(20)
                        .build(qrData.qrCode)
                        .render()

                    // Convert to compressed bitmap
                    val bitmap = BitmapFactory.decodeByteArray(
                        qrCode.getBytes(),
                        0,
                        qrCode.getBytes().size
                    )
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(
                        Bitmap.CompressFormat.PNG,
                        80,
                        stream
                    ) // Compress to 80% quality
                    val compressedBytes = stream.toByteArray()

                    // Create image data from compressed bytes
                    val imageData = ImageDataFactory.create(compressedBytes)
                    val image = Image(imageData)
                        .scaleToFit(100F, 100F)
                        .setMargins(10F, 10F, 5F, 10F)

                    // Clean up
                    bitmap.recycle()
                    stream.close()

                    val individualCouponTable = Table(floatArrayOf(30f, 70f))

                    individualCouponTable.addCell(
                        Cell()
                            .add(image)
                            .setBorder(Border.NO_BORDER)
                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                            .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    )
                    individualCouponTable.addCell(
                        Cell()
                            .add(
                                Paragraph()
                                    .add(
                                        Text("Kupon Qurban $currentYear\n")
                                            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD))
                                    )
                                    .add(
                                        Text("Nama: ${qrData.couponName}\n")
                                            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN))
                                    )
                                    .add(
                                        Text("Lokasi: ${location}\n")
                                            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN))
                                    )
                                    .add(
                                        Text("Tanggal/Waktu: $time")
                                            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN))
                                    )
                            )
                            .setVerticalAlignment(VerticalAlignment.MIDDLE)
                            .setHorizontalAlignment(HorizontalAlignment.LEFT)
                            .setBorder(Border.NO_BORDER)
                    )

                    table.addCell(individualCouponTable)
                }

                document.add(table)
                if (chunk != qrDataList.chunked(CHUNK_SIZE).last()) {
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

    companion object {
        private const val CHUNK_SIZE = 12
    }
}