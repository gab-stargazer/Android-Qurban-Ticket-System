package com.lelestargazer.qurban_ticketing_system.participant_management_system.data.qr

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
import qrcode.QRCode
import java.io.File
import java.util.Calendar

class QRGenerator {

    fun saveCoupons(qrDataList: List<QRGeneratorData>) {
        val documentsDir = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val file = File(documentsDir, "kupon-qurban-${currentYear}.pdf")
        val writer = PdfWriter(file)
        val pdf = PdfDocument(writer)

        val document = Document(pdf, PageSize.A4)
        document.setMargins(21F, 17.5F, 21F, 17.5F)

        qrDataList.chunked(16).forEach { chunk ->
            val table = Table(4)
            chunk.forEachIndexed { index, qrData ->
                val qrCode = QRCode.ofRoundedSquares()
                    .withSize(50)
                    .build(qrData.qrCode)
                    .render()

                val imageData = ImageDataFactory.create(qrCode.getBytes())
                val image = Image(imageData)
                    .scaleToFit(120F, 120F)
                    .setMargins(10F, 10F, 5F, 10F)

                val couponStatus = Text(qrData.couponStatus + "\n")
                    .setFontSize(10F)

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
    }

    fun getQrImage(qrHash: String): Bitmap {
        val qrCode = QRCode.ofRoundedSquares()
            .withSize(50)
            .build(qrHash)
            .render()

        return BitmapFactory.decodeByteArray(qrCode.getBytes(), 0, qrCode.getBytes().size)
    }

    data class QRGeneratorData(
        val qrCode: String,
        val couponStatus: String,
        val couponName: String,
    )
}