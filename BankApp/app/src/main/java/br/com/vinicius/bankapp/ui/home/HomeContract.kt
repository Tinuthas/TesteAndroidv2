package br.com.vinicius.bankapp.ui.home

import br.com.vinicius.bankapp.data.remote.model.StatementModel

class HomeContract {

    interface View {
        fun notification(message: String)

        fun initRecyclerView(models: List<StatementModel>)
    }

    interface Presenter {
        fun fetchListStatements(idUser:Int)
    }
}