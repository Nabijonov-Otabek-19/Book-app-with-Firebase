package uz.gita.bookappwithfirebase.presentation.ui.screens.explore

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.gita.bookappwithfirebase.data.common.AllBooksData
import uz.gita.bookappwithfirebase.data.common.BookData
import uz.gita.bookappwithfirebase.data.common.CategoryData
import uz.gita.bookappwithfirebase.domain.repository.impl.AppRepositoryImpl
import javax.inject.Inject

@HiltViewModel
class ExploreViewModelImpl @Inject constructor(
    private val repository: AppRepositoryImpl,
    private val direction: ExploreScreenDirection
) : ExploreViewModel, ViewModel() {

    override val booksData = MutableLiveData<List<AllBooksData>>()
    override val categoriesData = MutableLiveData<List<CategoryData>>()
    override val errorData = MutableLiveData<String>()

    override fun navigateToAboutBookScreen(bookData: BookData) {
        viewModelScope.launch {
            direction.navigateToAboutBookScreen(bookData)
        }
    }

    init {
        getAllData()
    }


    fun getBooksByCategory(categoryNameList: List<String>) {
        val list = ArrayList<AllBooksData>()
        for (index in categoryNameList.indices) {
            repository.getBooksByCategory(categoryNameList[index])
                .onEach { result ->
                    result.onSuccess {
                        list.add(AllBooksData(categoryNameList[index], it))
                        booksData.value = list
                    }
                    result.onFailure { errorData.value = it.message }
                }.launchIn(viewModelScope)
        }
    }

    private fun getAllData() {
        repository.getCategories()
            .onEach { categoryList ->
                categoryList.onSuccess { categoriesData.value = it }
                categoryList.onFailure { errorData.value = it.message }
            }.launchIn(viewModelScope)
    }
}