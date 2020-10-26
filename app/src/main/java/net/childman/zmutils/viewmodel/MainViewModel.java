package net.childman.zmutils.viewmodel;

import androidx.lifecycle.MutableLiveData;

import net.childman.libmvvm.viewmodel.BaseViewModel;

public class MainViewModel extends BaseViewModel {
    public final MutableLiveData<String> value = new MutableLiveData<>();
}
