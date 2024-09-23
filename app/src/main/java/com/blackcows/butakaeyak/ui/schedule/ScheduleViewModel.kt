package com.blackcows.butakaeyak.ui.schedule

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackcows.butakaeyak.data.models.Friend
import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.domain.repo.FriendRepository
import com.blackcows.butakaeyak.domain.repo.MedicineGroupRepository
import com.blackcows.butakaeyak.domain.repo.UserRepository
import com.blackcows.butakaeyak.ui.example.UserUiState
import com.blackcows.butakaeyak.ui.schedule.recycler.ScheduleProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val medicineGroupRepository: MedicineGroupRepository,
    private val userRepository: UserRepository,
    private val friendRepository: FriendRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<ScheduleUiState>(ScheduleUiState.Init)
    val uiState = _uiState.asStateFlow()

    private val _medicineGroup = MutableLiveData<List<MedicineGroup>>(listOf())
    val medicineGroup = _medicineGroup

    private val _friends = MutableLiveData<List<Friend>>(listOf())
    val friends = _friends

    private val _scheduleProfile = MutableLiveData<List<ScheduleProfile>>(listOf())
    val scheduleProfile = _scheduleProfile

    fun getMedicineGroup(userId: String) {
        viewModelScope.launch {
            _uiState.value = ScheduleUiState.Loading
            _medicineGroup.value = medicineGroupRepository.getMyGroups(userId)
            _uiState.value = ScheduleUiState.Success
        }
    }

    fun removeMedicineGroup(medicineGroup: MedicineGroup) {
        viewModelScope.launch {
            _uiState.value = ScheduleUiState.Loading
            medicineGroupRepository.removeGroup(medicineGroup)
            _uiState.value = ScheduleUiState.Success
        }
    }

    fun checkTakenMedicineGroup(medicineGroup: MedicineGroup, taken: Boolean, alarm: String) {
        viewModelScope.launch {
            _uiState.value = ScheduleUiState.Loading
            medicineGroupRepository.notifyTaken(medicineGroup, taken, alarm)
            _uiState.value = ScheduleUiState.Success
        }
    }

    fun getFriendProfile(userId: String) {
        viewModelScope.launch {
            _uiState.value = ScheduleUiState.Loading

            _scheduleProfile.value = friendRepository.getMyFriends(userId).map {
                val friendId = if(userId != it.proposer) it.proposer
                                else it.receiver
                userRepository.getProfileAndName(friendId)
            }

            _uiState.value = ScheduleUiState.Success
        }
    }


}