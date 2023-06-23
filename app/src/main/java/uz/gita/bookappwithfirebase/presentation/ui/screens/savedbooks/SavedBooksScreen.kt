package uz.gita.bookappwithfirebase.presentation.ui.screens.savedbooks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.bookappwithfirebase.R
import uz.gita.bookappwithfirebase.databinding.ScreenSavedBinding
import uz.gita.bookappwithfirebase.presentation.ui.adapters.SavedAdapter
import uz.gita.bookappwithfirebase.utils.logger
import uz.gita.bookappwithfirebase.utils.toasT
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class SavedBooksScreen : Fragment(R.layout.screen_saved) {

    private val binding by viewBinding(ScreenSavedBinding::bind)
    private val viewModel by viewModels<SavedViewModelImpl>()

    @Inject
    lateinit var adapter: SavedAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllData(requireContext())

        adapter.setClickListener {
            viewModel.navigateToReadBookScreen(it.name, 0, it.page.toInt())
        }

        adapter.setDeleteClickListener {
            val file = File(requireContext().filesDir, it.name)
            val deleted = if (file.exists()) file.delete() else false

            if (deleted) {
                toasT("Book deleted")
            } else {
                toasT("File not found")
                logger("File not found")
            }
            adapter.notifyDataSetChanged()
        }

        binding.apply {
            recycler.layoutManager = LinearLayoutManager(requireContext())
            recycler.adapter = adapter
        }

        viewModel.booksData.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.apply {
                    imgNoBooks.visibility = View.VISIBLE
                    txtNoBookTitle.visibility = View.VISIBLE
                }
            } else {
                binding.apply {
                    imgNoBooks.visibility = View.GONE
                    txtNoBookTitle.visibility = View.GONE
                }
            }
            adapter.setData(it)
        }

        viewModel.errorData.observe(viewLifecycleOwner) {
            logger("SavedScreen error = $it")
        }
    }
}