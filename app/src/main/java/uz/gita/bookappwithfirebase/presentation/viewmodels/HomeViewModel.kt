package uz.gita.bookappwithfirebase.presentation.viewmodels

import androidx.lifecycle.LiveData
import uz.gita.bookappwithfirebase.data.common.BookData

interface HomeViewModel {

    val booksData: LiveData<List<BookData>>
    val errorData: LiveData<String>
}