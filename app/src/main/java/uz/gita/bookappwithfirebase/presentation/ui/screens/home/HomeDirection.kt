package uz.gita.bookappwithfirebase.presentation.ui.screens.home

import uz.gita.bookappwithfirebase.data.common.BookData

interface HomeDirection {
    suspend fun navigateToAboutBookScreen(bookData: BookData)
}