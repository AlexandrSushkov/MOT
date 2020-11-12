package dev.nelson.mot.main.data.repository

import android.content.Context
import dev.nelson.mot.main.data.room.MotDatabase
import dev.nelson.mot.main.data.room.model.category.Category
import io.reactivex.Flowable
import javax.inject.Inject

//class CategoryRep @Inject constructor(private val motDatabase: MotDatabase){
class CategoryRep @Inject constructor(private val context: Context){

    private val categories = listOf(
        Category(1,"name1"),
        Category(2,"name2"),
        Category(3,"name3"),
        Category(4,"name4"),
        Category(5,"name5"),
        Category(6,"name6"),
        Category(7,"name7"),
        Category(8,"name8"),
        Category(9,"name9"),
        Category(9,"name10"),
        Category(9,"name11"),
        Category(9,"name12"),
        Category(9,"name13"),
        Category(9,"name14"),
        Category(9,"name15"),
        Category(9,"name16"),
        Category(9,"name17")
    )

    fun getCategories(): Flowable<List<Category>> = Flowable.just(categories)

}