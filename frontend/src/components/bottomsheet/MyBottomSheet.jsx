import './MyBottomSheet.css'
import { BottomSheet } from 'react-spring-bottom-sheet'
import { useEffect, useState } from 'react'
import 'react-spring-bottom-sheet/dist/style.css'

const MyBottomSheet = () => {

  const [open, setOpen] = useState(false)
  useEffect(() => {
    setOpen(true)
  }, [])

  function onDismiss() {
    setOpen(false)
  }

  return (
    <>
      <button onClick={() => setOpen(true)}>Open</button>
      <BottomSheet 
        open={open}
        onDismiss={onDismiss}
        snapPoints={({ minHeight }) => minHeight}
      >
        My bottomsheet
      </BottomSheet>
    </>
  )
}

export default MyBottomSheet