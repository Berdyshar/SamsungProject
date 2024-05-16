package com.example.samsungprojectlanglearner.UI;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.samsungprojectlanglearner.data.Dict.Dict;

import com.example.samsungprojectlanglearner.data.DictDataBase;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {
    private final DictDataBase dataBase;
    private final MutableLiveData<List<Dict>> dicts = new MutableLiveData<>();
    public MainViewModel(@NonNull Application application) {
        super(application);
        dataBase = DictDataBase.newInstance(application);

    }

    public LiveData<List<Dict>> getDicts() {
        return dicts;
    }
    public void remove(Dict dict) {
        Disposable disposable = dataBase.DictDao()
                .remove(dict.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                        refreshList();
                    }
                });
        compositeDisposable.add(disposable);
    }
    public void add(Dict dict) {
        Disposable disposable = dataBase.DictDao()
                .add(dict)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                        refreshList();
                    }
                });
        compositeDisposable.add(disposable);
    }
    public void update(Dict dict) {
        Disposable disposable = dataBase.DictDao()
                .update(dict.getId(), dict.getComps(), dict.getName(), dict.getResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                        refreshList();
                    }
                });
        compositeDisposable.add(disposable);
    }

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public void refreshList() {
        Disposable disposable = dataBase.DictDao()
                .getDicts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Dict>>() {
                    @Override
                    public void accept(List<Dict> dictsDB) throws Throwable {
                        dicts.setValue(dictsDB);
                    }
                });
        compositeDisposable.add(disposable);

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
