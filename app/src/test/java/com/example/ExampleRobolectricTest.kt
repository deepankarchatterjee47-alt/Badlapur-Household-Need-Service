package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Badlapur Household Need Service", appName)
    val phone = context.getString(R.string.business_phone)
    assertEquals("9284292348", phone)
  }

  @Test
  fun `verify material policy calculations`() {
    val serviceCharge = 450.0
    val materialChargeCustomerProvided = 0.0
    val totalCustomerProvided = serviceCharge + materialChargeCustomerProvided
    assertEquals(450.0, totalCustomerProvided, 0.01)

    val materialChargeTechnicianBrings = 320.0
    val totalTechnicianBrings = serviceCharge + materialChargeTechnicianBrings
    assertEquals(770.0, totalTechnicianBrings, 0.01)
  }
}
