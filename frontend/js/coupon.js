const coupon = (_ => {
	const listeners = {
		handleSubmit(event) {
			event.preventDefault()

			document.querySelectorAll('.validation-message').forEach(element =>
				element.innerText = '')

			const couponId = document.querySelector('input').value

			fetch(`http://localhost:5000/coupons/${couponId}`).then(response => {
				if (response.status === 400) {
					notify('error', 'Invalid request.')
					return new Promise(resolve => resolve())
				}

				else if (response.status === 404) {
					notify('warning', `Coupon ${couponId} is not found.`)
					return new Promise(resolve => resolve())
				}

				else if (response.status === 200) {
					return response.json()
				}

				else notify('error', 'A surprising error occurred.')
				return new Promise(resolve => resolve())
			}, _ => {
				notify('error', 'Unable to connect to the server.')
			}).then(coupon => {
				if (coupon && coupon.id) {
					notify('success', `Coupon ${coupon.id} is a valid coupon.`)
				}
			})
		},

		handleInputChange(event) {
			const isValid = event.target.checkValidity()
			if (isValid) {
				event.target.parentNode.querySelector('.validation-message').innerText = ''
			}
		},

		handleInvalid(event) {
			event.preventDefault()

			event.target.parentNode.querySelector('.validation-message').innerText =
				event.target.validationMessage
		},

		handleLogout() {
			auth.logout()
			location.hash = ''
		}
	}

	return {
		init() {
			if (!auth.isLoggedIn()) location.hash = 'login'

			document.querySelector('#coupon-form').addEventListener('submit',
				listeners.handleSubmit)

			document.querySelectorAll('input').forEach(element => {
				element.addEventListener('change', listeners.handleInputChange)
			})

			document.addEventListener('invalid', listeners.handleInvalid, true)

			document.querySelector('#logout').addEventListener('click',
				listeners.handleLogout)
		},

		cleanup() {
			document.querySelector('#coupon-form').removeEventListener('submit',
				listeners.handleSubmit)

			document.querySelectorAll('input').forEach(element => {
				element.removeEventListener('change', listeners.handleInputChange)
			})

			document.removeEventListener('invalid', listeners.handleInvalid, true)

			document.querySelector('#logout').removeEventListener('click',
				listeners.handleLogout)
		}
	}
})()
