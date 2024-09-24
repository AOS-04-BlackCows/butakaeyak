package com.blackcows.butakaeyak.ui.take.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.TakeAddMedicine
import com.blackcows.butakaeyak.databinding.FragmentNameBinding
import com.blackcows.butakaeyak.ui.getMedicineTypeToDrawable
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.take.FormSelectDialog
import com.blackcows.butakaeyak.ui.take.TakeAddViewModel
import com.blackcows.butakaeyak.ui.take.adapter.NameAdapter
import com.blackcows.butakaeyak.ui.take.data.NameItem

const val TAG = "NameFragment"
class NameFragment : Fragment() {

    //binding 설정
    private var _binding: FragmentNameBinding? = null
    private val binding get() = _binding!!
    private val takeAddViewModel : TakeAddViewModel by activityViewModels()
    private lateinit var adapter : NameAdapter
    private var addNamesImageUrl: String = "medicine_type_1"
    private var nameRvGroup = mutableListOf<TakeAddMedicine>()

    //data class
    private val mItems = mutableListOf<NameItem>()

    //TODO: 여기!
    private val onBackPressed = {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.move_enter, R.anim.move_exit)
            .remove(this)
            .commit()

        MainNavigation.popCurrentFragment()
    }

    //TODO: 여기!
    //bundle에서 medicine 가져오기
//    private val medicine: Medicine by lazy {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            arguments?.getParcelable(MEDICINE_DATA, Medicine::class.java)!!
//        } else {
//            @Suppress("DEPRECATION")
//            arguments?.getParcelable(MEDICINE_DATA)!!
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNameBinding.inflate(inflater, container, false)
        val root: View = binding.root

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d("NameFragment", "Back Pressed!")
                onBackPressed()
            }
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnMedicineForm.setOnClickListener {
                val formDialog = FormSelectDialog(requireContext(), object : FormSelectDialog.OnFormSelectListener {
                    override fun onImageSelected(drawableName: String, drawable: Drawable) {
                        addNamesImageUrl = drawableName
                        btnMedicineForm.setBackgroundResource(getMedicineTypeToDrawable(addNamesImageUrl))
                    }
                })
                formDialog.show()
            }

//            etMedicineName.setText(medicine.name!!)

//            val nameList

            nameRvGroup = takeAddViewModel.loadNames().toMutableList()
            adapter = NameAdapter(object : NameAdapter.ClickListener {
                override fun onMinusClick(item: TakeAddMedicine, position: Int) {
                    // TODO("Not yet implemented")
                    nameRvGroup.removeAt(position)
                    adapter.submitList(nameRvGroup.toList())
                    Log.d(TAG, "$nameRvGroup")
                }
                override fun onMedicineClick(item: TakeAddMedicine, position: Int) {
                    // TODO dadad
                    FormSelectDialog(root.context, object :
                        FormSelectDialog.OnFormSelectListener {
                        override fun onImageSelected(drawableName: String, background: Drawable) {
                            Log.d("drawableName", "drawableName = $drawableName")
                            Log.d("drawableName", "getMedicineTypeToDrawable(drawableName) = ${getMedicineTypeToDrawable(drawableName)}")
                            changeBackground(position, drawableName)
                            adapter.notifyItemChanged(position)
                        }
                    }).show()
                }
                override fun onSearchClick(item: TakeAddMedicine) {
                    // TODO("Not yet implemented")
                }
            })
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.setItemAnimator(null)
            // 프래그먼트에 진입했을 때 데이터가 있을시
            adapter.submitList(nameRvGroup.toList())

            btnPlus.setOnClickListener {
                addNames(addNamesImageUrl, etMedicineName.text.toString())
                // 초기화
                btnMedicineForm.setBackgroundResource(R.drawable.medicine_type_1)
                etMedicineName.text.clear()
                addNamesImageUrl = "medicine_type_1"
                adapter.submitList(nameRvGroup.toList())
                tvSize.text = "총 ${nameRvGroup.size}개의 약이 등록 예정"
                isNameItem(nameRvGroup)
            }
            btnNext.setOnClickListener {
                if(etMedicineName.length() > 0){
                    Log.d("버튼","버튼 눌림")
                    parentFragmentManager.beginTransaction()
                        .add(R.id.fragment_container, CycleFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }


        }

        //TODO: 여기!
        binding.ivBack.setOnClickListener {
            Log.d("NameFragment", "Back Pressed2")
            onBackPressed()
        }

        binding.etMedicineName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                binding.apply{
//                    if(etMedicineName.length() > 0){
//                        btnNext.apply{
//                            isEnabled = true
//                            setBackgroundResource(R.color.green)
//                            setTextColor(Color.WHITE)
//                        }
//                    }
//                    else{
//                        btnNext.apply{
//                            isEnabled = false
//                            setBackgroundResource(R.color.gray)
//                            setTextColor(Color.DKGRAY)
//                        }
//                    }
//                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        //editText 클릭 시 키보드 올리는 코드
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.apply{
                inputMethodManager.showSoftInput(etMedicineName, 0)
        }
    }

    private fun hideKeyboard(){
        if(activity != null && requireActivity().currentFocus != null){
            val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
    // 아이템이 비어있는지 체크
    private fun isNameItem (nameRvGroup: List<TakeAddMedicine>) {
        if(nameRvGroup.isNotEmpty()){
            binding.btnNext.apply{
                isEnabled = true
                setBackgroundResource(R.color.green)
                setTextColor(Color.WHITE)
            }
        } else {
            binding.btnNext.apply{
                isEnabled = false
                setBackgroundResource(R.color.gray)
                setTextColor(Color.DKGRAY)
            }
        }
    }
    private fun addNames(imageUrl: String, name: String) {
        nameRvGroup = nameRvGroup.apply {
            this.add(TakeAddMedicine(imageUrl, name, false))
        }
    }
    private fun changeBackground(position: Int, background: String) {
        nameRvGroup[position].imageUrl = background
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    companion object {
//        private const val MEDICINE_DATA = "medicine_data"
//
//        @JvmStatic
//        fun newInstance(medicine: Medicine) =
//            NameFragment().apply {
//                arguments = Bundle().apply {
//                    putParcelable(MEDICINE_DATA, medicine)
//                }
//            }
//    }
}