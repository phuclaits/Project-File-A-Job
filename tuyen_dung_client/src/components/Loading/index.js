import React from 'react'
import "./loading.css"
import { Modal } from 'reactstrap'
function LoadingPage() {

  return (
    
     <Modal isOpen='true' centered contentClassName='closeBorder modalloading' >

        <div className='loader1' />

    </Modal>
  )
}

export default LoadingPage