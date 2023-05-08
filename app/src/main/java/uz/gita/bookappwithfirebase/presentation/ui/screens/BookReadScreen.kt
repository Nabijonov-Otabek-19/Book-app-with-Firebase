package uz.gita.bookappwithfirebase.presentation.ui.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.github.barteksc.pdfviewer.util.FitPolicy
import uz.gita.bookappwithfirebase.R
import uz.gita.bookappwithfirebase.databinding.ScreenBookReadBinding
import uz.gita.bookappwithfirebase.utils.logd
import uz.gita.bookappwithfirebase.utils.toasT
import java.io.File

class BookReadScreen : Fragment(R.layout.screen_book_read), OnPageChangeListener,
    OnPageErrorListener {

    private val binding by viewBinding(ScreenBookReadBinding::bind)
    private val args by navArgs<BookReadScreenArgs>()

    private var pageNumber = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bookData = args.bookData
        val file = File(requireContext().filesDir, bookData!!.name)

        if (file.exists()) {
            binding.pdfView.useBestQuality(true)

            binding.txtPages.text = "1/${bookData.page}"

            binding.pdfView.fromFile(file)
                .enableSwipe(true)
                .defaultPage(pageNumber)
                .swipeHorizontal(true)
                .pageSnap(true)
                .autoSpacing(true)
                .pageFling(true)
                .enableDoubletap(true)
                .enableAnnotationRendering(false)
                .scrollHandle(DefaultScrollHandle(requireContext()))
                .onPageChange(this)
                .onPageError(this)
                .enableAntialiasing(true)
                .spacing(10)
                .nightMode(false)
                .pageFitPolicy(FitPolicy.BOTH)
                .load()
        } else {
            toasT("Book is not downloaded")
        }

        binding.apply {
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        pageNumber = page
        binding.txtPages.text = String.format("%s / %s", page + 1, pageCount)
    }


    override fun onPageError(page: Int, t: Throwable?) {
        logd("Cannot load page = $page")
    }
}